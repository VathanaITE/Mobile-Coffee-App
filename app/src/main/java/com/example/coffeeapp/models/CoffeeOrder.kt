package com.example.coffeeapp.models



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

data class OrderItem(
    val id: String = "",
    val coffeeImage: String = "",
    val coffeeName: String = "",
    val size: String = "",
    val sugarLevel: Int = 0,
    var quantity: Int = 0,
    val priceAtTime: Double = 0.0
)

