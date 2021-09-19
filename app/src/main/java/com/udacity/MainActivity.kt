package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        // notificationManager instance
        notificationManager = ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
        ) as NotificationManager
        createChannel(CHANNEL_ID, "channel")

        // register the BroadcastReciever
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download()

        }
    }

    // BroadcastReciever instance
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            // Reset download state
            custom_button.buttonState = ButtonState.Completed
            notificationManager.sendNotification("file download is complete", applicationContext)
        }
    }

    private fun download() {
        custom_button.buttonState = ButtonState.Loading
        val request =
                DownloadManager.Request(Uri.parse(URL))
                        .setTitle(getString(R.string.app_name))
                        .setDescription(getString(R.string.app_description))
                        .setRequiresCharging(false)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }
    // create notificationChannel

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download"
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    companion object {
        private const val URL =
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        const val CHANNEL_ID = "channelId"
    }

}
