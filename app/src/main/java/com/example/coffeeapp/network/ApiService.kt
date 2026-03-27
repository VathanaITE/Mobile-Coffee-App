package com.example.melodycoffeeapp.models

import com.example.coffeeapp.models.Category
import retrofit2.http.GET

interface ApiService {
    @GET("menu.json")
    suspend fun getCoffeeList(): List<Coffee>

    @GET("categories.json")
    suspend fun getCategories(): Map<String,Category>
}