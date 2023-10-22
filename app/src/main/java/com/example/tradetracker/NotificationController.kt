package com.example.tradetracker

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class NotificationController(private val context: Context, private val activity: Activity) {

    //contruct(){ }


    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Checks for the appropriate permissions
     */
    fun checkNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestRuntimePermissions()
            }
        }
    }

    /**
     * Grants the appropriate permissions
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestRuntimePermissions() {
        ActivityCompat.requestPermissions(activity, arrayOf(
            Manifest.permission.POST_NOTIFICATIONS
        ), Constants.NOTIFICATION_REQUEST_ID)
    }


}