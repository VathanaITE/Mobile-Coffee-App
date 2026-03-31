package com.example.coffeeapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeeapp.AppUtil
import com.example.coffeeapp.R
import com.example.coffeeapp.components.CustomInput
import com.example.coffeeapp.viewModels.AuthViewModel

@Composable
fun LoginScreen(authViewModel: AuthViewModel,navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Top Image section
        Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {
            Image(
                painter = painterResource(id = R.drawable.bannerlogo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Add a white gradient at the bottom of the image to blend into the background
        }

        // 2. Header Text
        Text(
            text = "Awaken Your Senses",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Your daily brew, just a tap away.",
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 3. Inputs (Using your CustomInput from earlier)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            CustomInput(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "Enter your email"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomInput(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                placeholder = "Enter your password",
                isPassword = true
            )
        }

        // 4. Log In Button
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authViewModel.login(email,password){success,message->
                        if(success){
                            navController.navigate("home"){
                                popUpTo("login"){
                                    inclusive = true
                                }
                            }
                        }else{
                            AppUtil.showToast(navController.context, message)
                        }
                    }
                }else{
                    AppUtil.showToast(navController.context, "Please enter both email and password")
                }

            },
            modifier = Modifier.fillMaxWidth().padding(24.dp).height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Log In", color = Color.White, fontSize = 18.sp)
        }

        // 5. Footer Link
        Row {
            Text("Don't have an account? ", color = Color.Gray)
            Text(
                text = "Sign up for free",
                color = Color(0xFFF59E0B),
                modifier = Modifier.clickable {
                    navController.navigate("signup")
                }
            )
        }
    }
}