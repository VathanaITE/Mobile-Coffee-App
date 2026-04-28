package com.example.coffeeapp.models


data class Coffee(
    val id: String? = null,
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val image: String = "",
    val sizes: Map<String, Double> = emptyMap()
)
