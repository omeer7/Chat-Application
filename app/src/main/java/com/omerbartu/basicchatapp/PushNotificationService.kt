package com.omerbartu.basicchatapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {

        val tittle=message.notification?.title
        val text = message.notification?.body
        val id= "HEADS_UP_NOTIFICATION"
        val notificationChannel: NotificationChannel
        notificationChannel= NotificationChannel(id,"Heads Up Notification",NotificationManager.IMPORTANCE_HIGH)
        getSystemService(NotificationManager::class.java).createNotificationChannel(notificationChannel)
        val notification = Notification.Builder(this,id)
            .setContentTitle(tittle)
            .setContentText(text)
            .setSmallIcon(R.drawable.send)
            .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(1,notification.build())
        super.onMessageReceived(message)
    }
}
