package com.udacity

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0
    //extension function to NotificationManager to send notifications
    fun NotificationManager.sendNotification(message:String, appContext: Context){
        val builder= NotificationCompat.Builder(appContext, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_assistant_black_24dp)
                .setContentTitle("Download Complete")
                .setContentText(message)
        notify(NOTIFICATION_ID, builder.build())
    }