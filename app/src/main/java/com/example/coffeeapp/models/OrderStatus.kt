package com.example.coffeeapp.models

enum class OrderStatus(val label: String) {
    PREPARING("Preparing"),
    CANCELED("Canceled"),
    READY("Ready"),
    COMPLETED("Completed")
}