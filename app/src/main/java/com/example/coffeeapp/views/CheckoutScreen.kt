package com.example.coffeeapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.coffeeapp.components.QrDialog
import com.example.coffeeapp.models.CoffeeOrder
import com.example.coffeeapp.viewModels.CartViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun CheckoutScreen(cartViewModel: CartViewModel, navController: NavController) {
    val context = LocalContext.current
    var showQrDialog by remember { mutableStateOf(false) }
    // Existing states
    var orderType by remember { mutableStateOf("Take Away") }
    val total = cartViewModel.getTotalPrice()
    // NEW: Payment Method State
    var paymentMethod by remember { mutableStateOf("") }
    val paymentOptions = listOf( "ABA/KHQR")

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 24.dp)) {

        IconButton(onClick ={ navController.popBackStack()}) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(cartViewModel.cartItems) { item ->
                Text("${item.coffeeName} x${item.quantity} - $${item.priceAtTime}")
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        // 2. Select Order Type (Take Away vs Dine In)
        Text("Order Type", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = orderType == "Take Away", onClick = { orderType = "Take Away" })
            Text("Take Away")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = orderType == "Dine In", onClick = { orderType = "Dine In" })
            Text("Dine In")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Payment Method",  fontWeight = FontWeight.Bold)

        // Payment Selection List
        paymentOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        paymentMethod = option
                    }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(selected = (paymentMethod == option), onClick = { paymentMethod = option })
                Text(text = option, modifier = Modifier.padding(start = 8.dp))
            }
        }

        // Total and Button
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Row {
        Text("Total: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("$${total}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Green)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (paymentMethod.isNotEmpty()) {
                    // Pass the paymentMethod to your ViewModel
                    showQrDialog= true
                }else{
                    AppUtil.showToast(context, "Please select a payment method")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.orange_app))
        ) {
            Text(text = "Confirm Order", fontSize = 20.sp)
        }
        if (showQrDialog) {
            QrDialog(
                onDismiss = { showQrDialog = false },
                onConfirm = {
                    showQrDialog = false
                    cartViewModel.placeOrder(items = cartViewModel.cartItems,orderType, paymentMethod, total) {
                        // Success: Clear the cart or show a "Thank You" screen
                        AppUtil.showToast(context,"Order placed successfully")
                    }
                    navController.navigate("success")
                })
        }
    }
}
