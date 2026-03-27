package com.example.coffeeapp.services

import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.example.coffeeapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//    }
    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            showNotification(it.title, it.body)
        }
    }

    private fun showNotification(title: String?, message: String?) {
        val builder = NotificationCompat.Builder(this, "coffee_channel")
            .setSmallIcon(R.mipmap.ic_launcher) // Your app icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(101, builder.build())
    }


}