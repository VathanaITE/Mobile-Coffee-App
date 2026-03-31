package com.example.coffeeapp.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.graphics.values
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.coffeeapp.MainActivity
import com.example.coffeeapp.models.Category
import com.example.coffeeapp.models.Coffee
import com.example.coffeeapp.models.RetrofitObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import kotlin.time.Duration.Companion.milliseconds

class CoffeeViewModel: ViewModel() {
    var isLoading by mutableStateOf(false)
    var categories by mutableStateOf<List<Category>>(emptyList())
    var selectedCategory by mutableStateOf("all") //key
    var coffeeList by mutableStateOf<List<Coffee>>(emptyList())
    var searchQuery by mutableStateOf("")

    init {
        viewModelScope.launch (Dispatchers.IO){
            // Move the heavy work to a background thread
            fetchData()
        }
    }

    fun fetchData(){
        isLoading= true
        viewModelScope.launch {
            try {
                // Launch both simultaneously
                val categoriesDeferred = async { RetrofitObject.api.getCategories() }
                val coffeeDeferred = async { RetrofitObject.api.getCoffeeList() }

                // Wait for both to complete
                categories = categoriesDeferred.await().values.toList()
                coffeeList = coffeeDeferred.await()

            }
            catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // The filtered list based on the selection
    val filteredCoffeeList: List<Coffee>
        get() {
            // 1. Filter by search query first
            val searchResult = if (searchQuery.isEmpty()) {
                coffeeList
            } else {
                coffeeList.filter { it.name.contains(searchQuery, ignoreCase = true) }
            }

            // 2. Filter by category dynamically
            return if (selectedCategory.equals("all", ignoreCase = true)) {
                searchResult
            } else {
                // This replaces the 'when' block.
                // It matches the coffee's category against the selected one automatically.
                searchResult.filter { it.category.equals(selectedCategory, ignoreCase = true) }
            }
        }
}