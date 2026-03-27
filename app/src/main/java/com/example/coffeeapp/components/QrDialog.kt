package com.example.coffeeapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.coffeeapp.R

@Composable
fun QrDialog(onDismiss: () -> Unit, onConfirm: () -> Unit){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "QR code") },
        text = {
            Column {
                Image(
                    painter = painterResource(R.drawable.qr),
                    contentDescription = "QR Code",
                    modifier = Modifier.height(300.dp),
                    contentScale = ContentScale.Crop
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(colorResource(R.color.orange_app))) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}