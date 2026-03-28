package com.example.coffeeapp.models.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coffeeapp.models.OrderItem


@Database(entities = [OrderItem::class], version = 1, exportSchema = false)
abstract class CoffeeDatabase: RoomDatabase() {
    abstract fun coffeeDao(): CoffeeDao

    companion object {
        @Volatile
        private var INSTANCE: CoffeeDatabase? = null

        fun getDatabase(context: Context): CoffeeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoffeeDatabase::class.java,
                    "coffee_database" // Database name
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance

                return instance
            }
        }
    }
}