package com.example.coffeeapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.coffeeapp.R
import com.example.melodycoffeeapp.viewModels.CoffeeViewModel

@Composable
fun CategoryTabs(viewModel: CoffeeViewModel) {
    val category = viewModel.categories
    LazyRow(contentPadding = PaddingValues(start=16.dp)) {
        items(category.size) {index ->
            val isSelected = viewModel.selectedCategory == category[index].id
            Surface(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { viewModel.selectedCategory = category[index].id },
                color = if (isSelected) colorResource(R.color.orange_app) else Color(0xE9F3F3F3),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = category[index].name,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = if (isSelected) Color.White else Color.Gray
                )
            }
        }
    }
}