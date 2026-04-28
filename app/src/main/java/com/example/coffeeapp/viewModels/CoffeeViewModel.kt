package com.example.coffeeapp.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeapp.models.Category
import com.example.coffeeapp.models.Coffee
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoffeeViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var categories by mutableStateOf<List<Category>>(emptyList())
    var selectedCategory by mutableStateOf("all")
    var coffeeList by mutableStateOf<List<Coffee>>(emptyList())
    var searchQuery by mutableStateOf("")

    init {
        // Enable persistence for offline access and faster startup
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        } catch (e: Exception) {
            // Persistence must be set before any other usage of FirebaseDatabase
            Log.d("CoffeeViewModel", "Persistence already enabled or error: ${e.message}")
        }
        
        loadInitialData()
    }

    private fun loadInitialData() {
        isLoading = true
        fetchCategories()
        fetchCoffee()
    }

    private fun fetchCategories() {
        val database = FirebaseDatabase.getInstance().getReference("category")
        // Use keepSynced to keep the data updated in local disk
        database.keepSynced(true)
        
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryList = mutableListOf<Category>()
                for (categorySnapshot in snapshot.children) {
                    val id = categorySnapshot.key
                    val name = categorySnapshot.child("name").getValue(String::class.java)
                    if (name != null) {
                        categoryList.add(Category(id, name))
                    }
                }
                categories = categoryList
                checkLoadingStatus()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Category fetch failed: ${error.message}")
                checkLoadingStatus()
            }
        })
    }

    private fun fetchCoffee() {
        val menuRef = FirebaseDatabase.getInstance().getReference("menu")
        menuRef.keepSynced(true)

        menuRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val menuList = mutableListOf<Coffee>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Coffee::class.java)?.copy(id = itemSnapshot.key)
                    if (item != null) {
                        menuList.add(item)
                    }
                }
                coffeeList = menuList
                checkLoadingStatus()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Menu fetch failed: ${error.message}")
                checkLoadingStatus()
            }
        })
    }
    
    private fun checkLoadingStatus() {
        // Simple logic: if we have data or if the first sync attempt finished, stop loading
        if (categories.isNotEmpty() || coffeeList.isNotEmpty()) {
            isLoading = false
        }
    }

    val filteredCoffeeList: List<Coffee>
        get() {
            val searchResult = if (searchQuery.isEmpty()) {
                coffeeList
            } else {
                coffeeList.filter { it.name.contains(searchQuery, ignoreCase = true) }
            }

            return if (selectedCategory.equals("all", ignoreCase = true)) {
                searchResult
            } else {
                searchResult.filter { it.category.equals(selectedCategory, ignoreCase = true) }
            }
        }
}
