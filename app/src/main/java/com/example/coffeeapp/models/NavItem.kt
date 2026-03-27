package com.example.coffeeapp.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)
val navItems = listOf(
    NavItem("Home", Icons.Outlined.Home,"home"),
    NavItem("Cart",Icons.Outlined.ShoppingCart,"cart"),
    NavItem("Order",Icons.Outlined.MailOutline,"order"),
    NavItem("Profile",Icons.Outlined.Person,"profile")
)
