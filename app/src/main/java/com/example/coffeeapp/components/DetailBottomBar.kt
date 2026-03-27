package com.example.coffeeapp.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeapp.models.OrderItem
import com.example.coffeeapp.viewModels.CartViewModel
import com.example.melodycoffeeapp.models.Coffee

@Composable
fun DetailBottomBar(price: Double,quantity: Int,cartViewModel: CartViewModel,
                    coffee: Coffee,selectedSize: String,sugarLevel: Int) {
    val context = LocalContext.current
    val total = price*quantity
    Surface(shadowElevation = 16.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Price", color = Color.Gray)
                Text(
                    "$${String.format("%.2f", total)}",
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                modifier = Modifier.height(56.dp).width(200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC67C4E)),
                onClick = {
                    val orderItem = OrderItem(
                        coffeeImage = coffee.image,
                        coffeeName = coffee.name,
                        size =selectedSize ,
                        sugarLevel = sugarLevel,
                        quantity = quantity,
                        priceAtTime = price
                    )
                    cartViewModel.addToCart(orderItem)
                    Toast.makeText(context,"Added to Cart",Toast.LENGTH_SHORT).show()

                },
            ) {
                Text("Add To Cart", fontSize = 18.sp)
            }
        }
    }
}





//fun DetailBottomBar(price: Double,quantity: Int,cartViewModel: CartViewModel) {
//    val context = LocalContext.current
//    val total = price*quantity
//    Surface(shadowElevation = 8.dp) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column {
//                Text("Price", color = Color.Gray)
//                Text(
//                    "$${String.format("%.2f", total)}",
//                    color = Color(0xFF2E7D32),
//                    style = MaterialTheme.typography.headlineSmall,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            Button(
//                modifier = Modifier.height(56.dp).width(200.dp),
//                shape = RoundedCornerShape(16.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC67C4E)),
//                onClick = {
//                    //cartViewModel.addToCart()
//                    Toast.makeText(context,"Added to Cart",Toast.LENGTH_SHORT).show()
//                },
//            ) {
//                Text("Add To Cart", fontSize = 18.sp)
//            }
//        }
//    }
//}