package com.example.coffeeapp.models

import retrofit2.http.GET

interface ApiService {
    @GET("menu.json")
    suspend fun getCoffeeList(): List<Coffee>

    @GET("categories.json")
    suspend fun getCategories(): Map<String,Category>
}