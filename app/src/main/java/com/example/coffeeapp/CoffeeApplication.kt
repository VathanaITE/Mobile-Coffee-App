package com.example.coffeeapp

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class CoffeeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // This ensures persistence is enabled ONLY ONCE at the very start
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}