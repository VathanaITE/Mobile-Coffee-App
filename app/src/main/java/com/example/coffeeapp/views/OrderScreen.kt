package com.example.coffeeapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeeapp.AppUtil
import com.example.coffeeapp.R
import com.example.coffeeapp.components.OrderItemCard
import com.example.coffeeapp.components.QrDialog
import com.example.coffeeapp.components.QrOrderGenerate
import com.example.coffeeapp.viewModels.CartViewModel


@Composable
fun OrderScreen( cartViewModel: CartViewModel,navController: NavController) {
    val context = LocalContext.current
    var selectedTabIndex by  remember { mutableIntStateOf(0) }
    val tabs = listOf("Preparing", "Completed","Canceled")
    val orders = cartViewModel.orderListState
    LaunchedEffect(selectedTabIndex) {
        cartViewModel.filterOrders(tabs[selectedTabIndex])
    }
    var showCompleteQrToStaff by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "My Orders",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.background(Color.White),
            contentColor = colorResource(R.color.orange_app) // Coffee Brown
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, color = if(selectedTabIndex == index) Color(0xFF6F4E37) else Color.Gray) }
                )
            }
        }
        if (orders.isNotEmpty()){
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)) {
                items(orders.size){ index->
                    val coffee = orders[index]
                    OrderItemCard(order = orders[index],
                        onCancelClick = {
                            cartViewModel.cancelOrder(coffee.orderId)
                        },
                        onCompleteQr = {
                            showCompleteQrToStaff = true
                        },
                        onDelete = {
                            cartViewModel.deleteOrder(coffee.orderId)
                        },
                        onReOrder = {
                            cartViewModel.prepareReorder(coffee.items)
                            navController.navigate("checkout")
                            AppUtil.showToast(context,"Your Order has been Add To Cart")
                        })
                    if (showCompleteQrToStaff){
                        AppUtil.showToast(context,"QR Generated")
                        QrOrderGenerate(
                            orderId = coffee.orderId,
                            onDismiss = { showCompleteQrToStaff = false },
                            onConfirm = { showCompleteQrToStaff = false }
                        )
                    }
                }
            }
         }
        else{
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Text(
                    text = "Your Order List is Empty",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray)
            }
        }
    }
}

