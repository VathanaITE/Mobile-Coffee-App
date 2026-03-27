package com.example.coffeeapp.components
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
//import com.google.maps.android.compose.GoogleMap
//import com.google.maps.android.compose.Marker
//import com.google.maps.android.compose.MarkerState
//import com.google.maps.android.compose.rememberCameraPositionState
//
//@Composable
//fun CoffeeMap() {
//    // Define the shop location
//    val shopLocation = LatLng(11.569, 104.891) // Example: Rupp coordinates
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(shopLocation, 15f)
//    }
//
//    // 2. The Map Container
//    Box(modifier = Modifier.fillMaxSize()) {
//        GoogleMap(
//            modifier = Modifier.matchParentSize(),
//            cameraPositionState = cameraPositionState
//        ) {
//            // Add a pin for your coffee shop
//            Marker(
//                state = MarkerState(position = shopLocation),
//                title = "RUPP Coffee Central",
//                snippet = "Open now"
//            )
//        }
//    }
//}