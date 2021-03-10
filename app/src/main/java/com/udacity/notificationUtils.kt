package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0

//extension function to NotificationManager to send notifications
fun NotificationManager.sendNotification(message: String, appContext: Context) {
    // create an Intent with the app context and the MainActivity that will be launched
    val contentIntent = Intent(appContext, DetailActivity::class.java)
    // create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
            appContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(appContext, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle("Download Complete")
            .setContentText(message)
            //set contentIntent
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
    notify(NOTIFICATION_ID, builder.build())
}