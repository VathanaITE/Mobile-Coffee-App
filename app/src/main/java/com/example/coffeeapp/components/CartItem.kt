package com.example.coffeeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.coffeeapp.R
import com.example.coffeeapp.models.OrderItem
import com.example.coffeeapp.viewModels.CartViewModel


@Composable
fun CartItem(orderItem: OrderItem,cartViewModel: CartViewModel){
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()){

        Row(modifier = Modifier.padding(8.dp)
            ,verticalAlignment = Alignment.CenterVertically) {

            AsyncImage(
                model = orderItem.coffeeImage,
                contentDescription = orderItem.coffeeName,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = orderItem.coffeeName,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text(
                    text = "Size: ${orderItem.size}, qty: ${orderItem.quantity}",
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = "Sugar level: ${orderItem.sugarLevel}%",
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text(
                    text = "$ ${orderItem.priceAtTime}",
                    color = colorResource(R.color.orange_app),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))
            IconButton(
                onClick = {
                    cartViewModel.removeFromCart(orderItem)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    }
}