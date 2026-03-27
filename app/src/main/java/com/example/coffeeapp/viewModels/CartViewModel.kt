package com.example.coffeeapp.viewModels

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.coffeeapp.models.CoffeeOrder
import com.example.coffeeapp.models.OrderItem
import com.example.coffeeapp.models.OrderStatus
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CartViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance().getReference("orders")
    private val uid = Firebase.auth.currentUser?.uid
    val orderListState = mutableStateListOf<CoffeeOrder>()
    var cartItems = mutableStateListOf<OrderItem>()


    val cartItemsCounts: Int
        get() = cartItems.sumOf { it.quantity }

    fun addToCart(item: OrderItem) {
        cartItems.add(item)
    }
    fun removeFromCart(item: OrderItem) {
        cartItems.remove(item)
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
        // Step 1: Query for only this user's orders
        val query = db.orderByChild("userId").equalTo(uid)

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
        val uid = Firebase.auth.currentUser?.uid ?: return
        val orderId = db.push().key ?: "" // Generates a unique ID for the order

        val newOrder = CoffeeOrder(
            orderId = orderId,
            userId = uid,
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