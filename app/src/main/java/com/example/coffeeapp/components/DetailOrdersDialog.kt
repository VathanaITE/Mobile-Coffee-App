package com.example.coffeeapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.coffeeapp.R
import com.example.coffeeapp.models.CoffeeOrder
import com.example.coffeeapp.viewModels.CartViewModel

@Composable
fun DetailOrdersDialog(order: CoffeeOrder, onClick: () -> Unit, onDismiss: () -> Unit){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "orders items detail") },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(order.items) { coffee ->
                    Text(text = "${coffee.coffeeName} " +
                            "x${coffee.quantity} " +
                            "size: ${coffee.size} "+
                            "sugar: ${coffee.sugarLevel}% "+
                            "price: ${coffee.priceAtTime}$" )
                }
            }
        },
        confirmButton = {
            Button(onClick = onClick,
                colors = ButtonDefaults.buttonColors(colorResource(R.color.orange_app))) {
                Text("ok")
            }
        },
    )
}