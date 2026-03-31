package com.example.coffeeapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeapp.models.CoffeeOrder
import com.example.coffeeapp.models.OrderItem
import com.example.coffeeapp.models.OrderStatus
import com.example.coffeeapp.models.roomDb.CoffeeDao
import com.example.coffeeapp.models.roomDb.CoffeeDatabase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CartViewModel(application: Application) : AndroidViewModel(application)  {
    private val db = FirebaseDatabase.getInstance().getReference("orders")
    private val uid = Firebase.auth.currentUser?.uid
    val orderListState = mutableStateListOf<CoffeeOrder>()
    var cartItems = mutableStateListOf<OrderItem>()
    val statusList = listOf(OrderStatus.PREPARING.label,OrderStatus.READY.label,OrderStatus.CANCELED.label)
    private val dbRoom by lazy { CoffeeDatabase.getDatabase(getApplication()) }
    private val coffeeDao: CoffeeDao by lazy { dbRoom.coffeeDao() }

    init {
        loadItemOrdersRoomDb()
    }

    private fun loadItemOrdersRoomDb() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val savedItems = coffeeDao.getAllOrderItems()
                withContext(Dispatchers.Main) {
                    cartItems.clear()
                    cartItems.addAll(savedItems)
                    Log.d("CartViewModel", "Loaded ${savedItems.size} items from Room")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error loading from Room: ${e.message}")
            }
        }
    }


    val cartItemsCounts: Int
        get() = cartItems.sumOf { it.quantity }

    fun addToCart(newItem: OrderItem) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Find by attributes, NOT ID
            val existingItem = cartItems.find {
                it.coffeeName == newItem.coffeeName &&
                        it.size == newItem.size &&
                        it.sugarLevel == newItem.sugarLevel
            }

            if (existingItem != null) {
                // 2. Update existing object
                existingItem.quantity += newItem.quantity
                coffeeDao.insertItem(existingItem)

                // 3. DO NOT manually add to cartItems here.
                // If cartItems is a state-backed list, update the list element or re-fetch.
            } else {
                // 4. Truly new item
                coffeeDao.insertItem(newItem)
                cartItems.add(newItem)
            }
        }
    }

    fun removeFromCart(item: OrderItem) {
        cartItems.remove(item)
        viewModelScope.launch{
            coffeeDao.deleteItem(item)
        }
    }
    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.priceAtTime * it.quantity }
    }

    fun cancelOrder(orderId: String) {
        db.child(orderId).child("status").setValue(OrderStatus.CANCELED.label)
    }
    fun deleteOrder(orderId: String) {
        db.child(orderId).removeValue()
    }
    fun prepareReorder(oldItems: List<OrderItem>) {
        // 1. Loop through each item in the old order
        oldItems.forEach { oldItem ->
            // 2. Check if the item is already in the cart to avoid duplicates
            val existingItem = cartItems.find { it.id == oldItem.id }
            if (existingItem != null) {
                // Increase quantity if it's already there
                existingItem.quantity += oldItem.quantity
            } else {
                // Add as a new item if it's not in the cart
                cartItems.add(oldItem)
            }
        }
    }

    fun filterOrders(targetStatus: String){
        val currentUid = uid ?: return
        // Step 1: Query for only this user's orders
        val query = db.orderByChild("userId").equalTo(currentUid)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filteredList = mutableListOf<CoffeeOrder>()

                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(CoffeeOrder::class.java)

                    // Step 2: Filter the status manually in Kotlin
                    if (order != null && order.status == targetStatus) {
                        filteredList.add(order)
                    }
                }
                // Update your UI state list here
                orderListState.clear()
                orderListState.addAll(filteredList)
                orderListState.sortByDescending { it.timestamp } // Sort by timestamp= filteredList.reversed() // .reversed() puts newest first
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Filter failed: ${error.message}")
            }
        })
    }
    fun placeOrder(items:List<OrderItem>,type: String,paymentMethod: String, total: Double, onSuccess: () -> Unit) {
        val currentUid = uid ?: return
        val orderId = db.push().key ?: "" // Generates a unique ID for the order

        val newOrder = CoffeeOrder(
            orderId = orderId,
            userId = currentUid,
            items = items,
            orderType = type,
            paymentMethod = paymentMethod,
            totalPrice = total,
            status = OrderStatus.PREPARING.label,
            timestamp = formatTimestamp(System.currentTimeMillis())
        )
        db.child(orderId).setValue(newOrder).addOnSuccessListener {
            // IMPORTANT: Clear the cart after ordering!
            cartItems.clear()
            onSuccess()
            viewModelScope.launch {
                coffeeDao.clearCart()
            }
        }
    }
    fun formatTimestamp(timestamp: Long): String {
        // Create a date object from the timestamp
        val date = Date(timestamp)
        // Choose your pattern:
        // "hh:mm a" -> 10:30 PM
        // "HH:mm"   -> 22:30 (24-hour format)
        //val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        return formatter.format(date)
    }

}
