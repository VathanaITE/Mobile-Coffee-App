package com.example.coffeeapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey


data class CoffeeOrder(
    val orderId: String = "",
    val userId: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: String = "Preparing", // Preparing, Ready, Completed
    val orderType: String = "", //
    val paymentMethod: String = "",
    val timestamp: String = ""
)

@Entity(tableName = "cart_items")
data class OrderItem(
    @PrimaryKey
    var id: String = "",
    val coffeeImage: String = "",
    val coffeeName: String = "",
    val size: String = "",
    val sugarLevel: Int = 0,
    var quantity: Int = 0,
    val priceAtTime: Double = 0.0
)

