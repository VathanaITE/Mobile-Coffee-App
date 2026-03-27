package com.example.coffeeapp.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.coffeeapp.models.navItems
import com.example.coffeeapp.viewModels.CartViewModel

@Composable
fun BottomNav(navController: NavController,cartViewModel: CartViewModel){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = Color(0xFFF5F5F5),
        contentColor = Color.White
    ) {
        navItems.forEach {  item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                label = { Text(item.title) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.route == "cart"&& cartViewModel.cartItems.isNotEmpty()) {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) { Text(cartViewModel.cartItemsCounts.toString()) }
                            }
                        }
                    ) {
                        Icon(item.icon, contentDescription = item.title)
                    }
                       },
                onClick = {
                    navController.navigate(item.route) {
                        //Pop up to the start destination to avoid building up a large stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }

    }
}