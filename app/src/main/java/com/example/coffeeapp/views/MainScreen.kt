package com.example.coffeeapp.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coffeeapp.viewModels.AuthViewModel
import com.example.coffeeapp.viewModels.CartViewModel
import com.example.coffeeapp.components.BottomNav
import com.example.melodycoffeeapp.viewModels.CoffeeViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(){
    val coffeeViewModel: CoffeeViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val noNavRoute = listOf<String>("login","signup","checkout","success")
    // 1. Get current destination
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isLogin = Firebase.auth.currentUser!=null
    val startDestination = if(isLogin) "home" else "login"
    val noPadding = listOf<String>("login","home")
    var paddingValues: PaddingValues

    Scaffold(
        bottomBar = {
            if (currentRoute !in noNavRoute){
                BottomNav(navController)
            }
        }
    ) {innerPadding->
        if(currentRoute !in noPadding){
            paddingValues=innerPadding
        }else{
            paddingValues= PaddingValues(bottom = innerPadding.calculateBottomPadding())
        }
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("signup") {
                SignUpScreen(authViewModel,navController)
            }
            composable("login") {
                LoginScreen(authViewModel,navController)
            }
            composable("home") { HomeScreen(coffeeViewModel, navController = navController,authViewModel) }

            composable("detail/{mealId}") {
                val mealId = it.arguments?.getString("mealId") ?: ""
                CoffeeDetail(mealId,coffeeViewModel,navController,cartViewModel)
            }
            composable("cart") {
                CartScreen(cartViewModel,navController)
            }
            composable("order") {
                OrderScreen(cartViewModel,navController)
            }
            composable("profile") {
                ProfileScreen(authViewModel,navController)
            }
            composable("checkout") {
                CheckoutScreen(cartViewModel,navController)
            }
            composable("success") {
                SuccessScreen(navController)
            }


        }
    }
}

