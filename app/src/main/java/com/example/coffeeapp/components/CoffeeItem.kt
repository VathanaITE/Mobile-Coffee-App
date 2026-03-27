package com.example.coffeeapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coffeeapp.R
import com.example.coffeeapp.models.Coffee


@Composable
fun CoffeeItem(coffee: Coffee, navController: NavController) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp),
        onClick = {navController.navigate("detail/${coffee.id}")}
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = coffee.image,
                contentDescription = coffee.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop // Ensures image fills space beautifully
            )
                Text(
                    text = coffee.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = coffee.category,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    color = Color.Gray,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$ ${coffee.sizes.values.last()}",
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 18.sp,
                    color = colorResource(R.color.orange_app),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 8.dp, top = 4.dp)
                )

        }
    }
}