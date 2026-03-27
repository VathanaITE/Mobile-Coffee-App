package com.example.coffeeapp.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.coffeeapp.R
import com.example.coffeeapp.components.CategoryTabs
import com.example.coffeeapp.components.CoffeeHeader
import com.example.coffeeapp.components.CoffeeItem
import com.example.coffeeapp.viewModels.AuthViewModel
import com.example.coffeeapp.viewModels.CoffeeViewModel

import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: CoffeeViewModel, navController: NavController, authViewModel: AuthViewModel){
    LaunchedEffect(Unit){
        authViewModel.fetchUserData()
    }
    val currentUser = LocalTime.now()
    val greeting = when (currentUser.hour) {
        in 0..11 -> {
            "Good Morning"
        }
        in 12..16 -> {
            "Good Afternoon"
        }
        in 17..20 -> {
            "Good Evening"
        }
        else -> {"Hello"}
    }
    val userName by authViewModel.userName

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    ) {
        CoffeeHeader(userName,greeting,viewModel,navController)
        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.isLoading){
            Box(modifier = Modifier.fillMaxSize(),
                Alignment.Center){
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = colorResource(R.color.orange_app)
                )
            }
        }else{
            CategoryTabs(viewModel)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Menu",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
            if (viewModel.filteredCoffeeList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = "No Coffee Found",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold)
                }
            }else{
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp)
                ) {

                    items(viewModel.filteredCoffeeList.size) { index ->
                        CoffeeItem(viewModel.filteredCoffeeList[index],navController) // Your existing card component
                    }
                }
            }
        }

    }
}
