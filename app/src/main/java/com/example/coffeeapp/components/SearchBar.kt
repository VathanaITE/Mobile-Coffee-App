//package com.example.coffeeapp.components
//
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//@Composable
//fun SearchBar() {
//    viewModel: CoffeeViewModel = viewModel()
//    TextField(
//        value =viewModel.searchQuery,
//        onValueChange = {newQuery->
//            viewModel.searchQuery = newQuery
//        },
//        placeholder = { Text("Find your favorite coffee...", color = Color.LightGray) },
////        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
//        trailingIcon = {
//            Box(
//                modifier = Modifier
//                    .padding(4.dp)
//                    .size(40.dp)
//                    .background(Color(0xFFF59E0B), RoundedCornerShape(10.dp)),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
//            }
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(4.dp, RoundedCornerShape(16.dp)),
//        shape = RoundedCornerShape(16.dp),
//        colors = TextFieldDefaults.colors(
//            focusedContainerColor = Color.White,
//            unfocusedContainerColor = Color.White,
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent
//        )
//    )
//}