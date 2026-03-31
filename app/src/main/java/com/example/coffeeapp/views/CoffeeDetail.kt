package com.example.coffeeapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coffeeapp.viewModels.CartViewModel
import com.example.coffeeapp.components.DetailBottomBar
import com.example.coffeeapp.viewModels.CoffeeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetail(coffeeId: String, viewModel: CoffeeViewModel,navController: NavController,cartViewModel: CartViewModel){
    val coffee = viewModel.coffeeList.find { it.id == coffeeId}
    // State to track selected size
    var selectedSize by remember { mutableStateOf("Small") }
    var sugarLevel by remember { mutableStateOf(100) }
    // Derived price based on selection
    val currentPrice = coffee?.sizes[selectedSize] ?: 0.0
    var quantity by remember { mutableStateOf(1) }

    if (coffee!=null){
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column (modifier = Modifier.padding(start = 16.dp,end = 16.dp).background(Color.White)){
                Box(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth(),
                        color = Color.White
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    modifier = Modifier.size(28.dp),
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 120.dp)
                ) {

                    // 1. Image
                    AsyncImage(
                        model = coffee.image,
                        contentDescription = coffee.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Name and Category
                    Text(
                        coffee.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(coffee.category, color = Color.DarkGray)


                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Size Selector
                    Text("Size", fontWeight = FontWeight.Bold,color = Color.Black)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val coffeeSize = coffee.sizes.keys // maybe not good at this point
                        coffeeSize.reversed().forEach { string ->
                            SizeButton(
                                label = string.first().toString(), // Shows S, M, or L
                                isSelected = selectedSize == string,
                                onClick = { selectedSize = string }
                            )
                        }
                    }

                    //3. sugar level
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Sugar Level %", fontWeight = FontWeight.Bold,color = Color.Black)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(0, 25, 50, 75, 100).forEach { level ->
                            SizeButton(
                                label = level.toString(),
                                isSelected = sugarLevel == level,
                                onClick = { sugarLevel = level }
                            )
                        }

                    }


                    //4.quantity
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Quantity", fontWeight = FontWeight.Bold,color = Color.Black)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(Color.White),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            if (quantity > 1) {
                                quantity--
                            }
                        },
                            modifier = Modifier.background(Color.White)) {
                            Text(text = "-", color = Color.Red, fontSize = 28.sp)
                        }
                        Text(text = quantity.toString(), color = Color.Black)
                        IconButton(onClick = {
                            quantity++
                        },
                            modifier = Modifier.background(Color.White)) {
                            Text(text = "+", color = Color.Green, fontSize = 28.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // 5. Description
                    Text(
                        "Description",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = coffee.description,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Keeps it stuck to the bottom
                    .fillMaxWidth().background(Color.White),
                shadowElevation = 2.dp,
                color = Color.White
            ) {
                DetailBottomBar(currentPrice, quantity,cartViewModel,coffee,selectedSize,sugarLevel)
            }
        }
    }
}




@Composable
fun RowScope.SizeButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) Color(0xFFC67C4E) else Color(0xFFEAEAEA)
    val bgColor = if (isSelected) Color(0xFFFFF5EE) else Color.White

    Box(
        modifier = Modifier
            .weight(1f)
            .height(45.dp)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(bgColor, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = if (isSelected) Color(0xFFC67C4E) else Color.Black)
    }
}
