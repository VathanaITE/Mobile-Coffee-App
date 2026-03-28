package com.example.coffeeapp.models.roomDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coffeeapp.models.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CoffeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: OrderItem)

    @Query("SELECT * FROM cart_items")
    fun getAllOrderItems(): List<OrderItem>

    @Delete
    suspend fun deleteItem(item: OrderItem)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}