package com.example.coffeeapp.views

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.coffeeapp.viewModels.CartViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeapp.components.CartItem

@Composable
fun CartScreen(cartViewModel: CartViewModel,navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "My Cart",
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Divider()
        if (cartViewModel.cartItems.isNotEmpty()){
            Box(modifier = Modifier.fillMaxSize()){
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(start = 16.dp,end = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartViewModel.cartItems.size) {it->
                        val coffee = cartViewModel.cartItems[it]
                        CartItem(coffee,cartViewModel)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                    shadowElevation = 16.dp

                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Column {
                            Text("Total", color = Color.Gray)
                            Text(
                                "$${String.format("%.2f", cartViewModel.getTotalPrice())}",
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
                                navController.navigate("checkout")
                            }
                        ){
                            Text(
                                text = "Check Out ->",
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }else{
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Text(
                    text = "Your cart is empty",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray)
            }
        }

    }
}