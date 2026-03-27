package com.example.melodycoffeeapp.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.coffeeapp.MainActivity
import com.example.coffeeapp.models.Category
import com.example.melodycoffeeapp.models.Coffee
import com.example.melodycoffeeapp.models.RetrofitObject
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import kotlin.time.Duration.Companion.milliseconds

class CoffeeViewModel(): ViewModel() {
    var isLoading by mutableStateOf(false)
    var categories by mutableStateOf<List<Category>>(emptyList())
    var selectedCategory by mutableStateOf("") //key
    var coffeeList by mutableStateOf<List<Coffee>>(emptyList())
    var searchQuery by mutableStateOf("")
    private val db = FirebaseDatabase.getInstance().getReference("categories")


    init {
        fetchData()
    }

    private fun fetchData(){
        isLoading = true
        try {
        fetchCoffeeList()
        fetchCategory()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isLoading = false
    }
    private fun fetchCategory() {
        viewModelScope.launch {
            try {
                // Get the Map from Firebase
                val categoryList = RetrofitObject.api.getCategories()
                if (categoryList.isNotEmpty()){
                    categories = categoryList.values.toList()
                    selectedCategory = categories[0].id
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun fetchCoffeeList() {
        viewModelScope.launch {
            try {
                coffeeList = RetrofitObject.api.getCoffeeList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // The filtered list based on the selection
    val filteredCoffeeList: List<Coffee>
        get() =
            if (searchQuery.isEmpty()) {
                when (selectedCategory) {
                    "all" -> coffeeList
                    "hot" -> coffeeList.filter { it.category == "Hot" }
                    "cold" -> coffeeList.filter { it.category == "Cold" }
                    else -> coffeeList
                }
            }else{
                val coffeeFilter = coffeeList.filter { it.name.contains(searchQuery, ignoreCase = true) }
                when (selectedCategory) {
                    "all" -> coffeeFilter
                    "hot" -> coffeeFilter.filter { it.category == "Hot" }
                    "cold" -> coffeeFilter.filter { it.category == "Cold" }
                    else -> coffeeFilter
                }
            }


}