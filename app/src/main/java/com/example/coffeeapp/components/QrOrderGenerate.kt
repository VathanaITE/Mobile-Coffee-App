package com.example.coffeeapp.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.coffeeapp.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

@Composable
fun QrOrderGenerate(orderId:String,onDismiss: () -> Unit, onConfirm: () -> Unit){
    // 1. Use remember so the QR is only generated once per orderId
    val qrBitmap = remember(orderId) {
        val writer = MultiFormatWriter()
        try {
            val bitMatrix = writer.encode(orderId, BarcodeFormat.QR_CODE, 512, 512)
            val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
            for (x in 0 until 512) {
                for (y in 0 until 512) {
                    // FIX: Use android.graphics.Color (Integers) instead of Compose Colors
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Generate QR") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Show this to the counter")
                Spacer(modifier = Modifier.height(16.dp))
                // 2. Display the generated bitmap
                qrBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(), // Convert android.graphics.Bitmap to ImageBitmap
                        contentDescription = "QR Code",
                        modifier = Modifier.size(250.dp)
                    )
                } ?: Text("Error generating QR")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(colorResource(R.color.orange_app))) {
                Text("Ok")
            }
        },
    )
}