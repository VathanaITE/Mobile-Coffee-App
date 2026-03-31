package com.example.coffeeapp.views

import android.content.Context
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeeapp.AppUtil
import com.example.coffeeapp.components.ChangeName
import com.example.coffeeapp.components.ChangePasswordDialog
import com.example.coffeeapp.viewModels.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.sql.Driver

@Composable
fun ProfileScreen(authViewModel: AuthViewModel, navController: NavController) {
    val context = LocalContext.current
    // Observe the username from your ViewModel
    val userName by authViewModel.userName
    val userEmail = Firebase.auth.currentUser?.email ?: "No Email Found"
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showChangeNameDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // Profile Picture Placeholder
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(Color(0xFF6F4E37), shape = CircleShape), // Coffee Brown
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(),
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = userName,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = userEmail,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProfileInfoCard(label = "Edit UserName", onClick = { showChangeNameDialog = true })
            if (showChangeNameDialog) {
                ChangeName(
                    onDismiss = { showChangeNameDialog = false },
                    onConfirm = { pass ->
                        authViewModel.editUserName(pass) { success, msg ->
                            showChangeNameDialog = false
                            if (!success) {
                                AppUtil.showToast(context, msg)
                            }
                        }
                    }
                )
            }


            ProfileInfoCard(
                label = "Change Password",
                onClick = { showChangePasswordDialog = true })
            if (showChangePasswordDialog) {
                ChangePasswordDialog(
                    onDismiss = { showChangePasswordDialog = false },
                    onConfirm = { pass ->
                        authViewModel.updatePassword(pass) { success, msg ->
                            showChangePasswordDialog = false
                            if (!success) {
                                AppUtil.showToast(context, msg)
                            }
                        }
                    }
                )
            }

            ProfileInfoCard(label = "Order History",
                onClick = {
                    navController.navigate("order-history")
                })

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            Button(
                onClick = {
                    Firebase.auth.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Logout", color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileInfoCard(label: String, onClick: () -> Unit = {}) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = label, color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onClick
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "edit")
            }

        }
    }
}