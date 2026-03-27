package com.example.coffeeapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.coffeeapp.R
import com.example.coffeeapp.models.CoffeeOrder

@Composable
fun OrderItemCard(order: CoffeeOrder, onCancelClick: () -> Unit,onCompleteQr: () -> Unit,onDelete:()->Unit,onReOrder:()->Unit) {
    var showDetailDialog by remember { mutableStateOf(false) }
    if (showDetailDialog){
        DetailOrdersDialog( order, onClick = { showDetailDialog = false }, onDismiss = {showDetailDialog=false})
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { showDetailDialog = true}
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Upper Section: Image and Details
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 1. Coffee Image
                AsyncImage(
                    model = order.items.firstOrNull()?.coffeeImage ?: "", // Gets first item image
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                // 2. Text Information
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (order.items.size>1){
                            "${order.items.first().coffeeName} and ${ order.items.size -1 } others"
                        } else {
                            order.items.firstOrNull()?.coffeeName ?: "unknow name"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(0xFF4E342E) // Dark Coffee Brown
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "CoffeeShop",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. Take Away Tag
                    Surface(
                        color = Color(0xFFFDF0F0), // Light pink/red background
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = order.orderType,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color(0xFFE57373),
                            fontSize = 14.sp
                        )
                    }
                }

                // 4.Time on the right
                Text(
                    text = order.timestamp,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            // CONDITIONAL BUTTON LOGIC
            when (order.status) {
                "Preparing" -> {
                    Button(
                        onClick = onCancelClick,
                        modifier = Modifier.fillMaxWidth().height(45.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        Text("Cancel Order")
                    }
                }
                "Canceled" -> {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround) {
                        Button(
                            onClick = onReOrder,
                            modifier = Modifier.weight(1f).height(45.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(colorResource(R.color.dark_green))
                        ) {
                            Text("Re-Order")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = onDelete,
                            modifier = Modifier.weight(1f).height(45.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Text("Delete")
                        }
                    }
                }
                "Ready" -> {
                    Button(
                        onClick = onCompleteQr,
                        modifier = Modifier.fillMaxWidth().height(45.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.dark_green))
                    ) {
                        Text("QR")
                    }
                }
            }
        }
    }
}