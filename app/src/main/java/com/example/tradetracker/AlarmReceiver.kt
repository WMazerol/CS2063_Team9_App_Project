package com.example.tradetracker

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.tradetracker.entity.Trade
import com.example.tradetracker.entity.TradeManager
import com.example.tradetracker.model.TradeViewModel
import java.time.Duration
import java.time.LocalDateTime

class AlarmReceiver : BroadcastReceiver() {

    //private var tradeViewModel = ViewModelProvider(MainActivity())[TradeViewModel::class.java]

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        Log.i("TradeTracker - AlarmReceiver", "PLACEHOLDER")

        val tradesBeyondStopOrTake = MainActivity().getTradesPastStopOrTake()

        for(trade in tradesBeyondStopOrTake) {
            if(Duration.between(trade.formatStringToDate(trade.lastNotified!!), LocalDateTime.now()).toHours() > 2) {

                val openIntent = Intent(context, MainActivity::class.java)
                val pendingIntent: PendingIntent =
                    PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_IMMUTABLE)

                val contentText = trade.symbol + " has gone " +
                        if(trade.stopLoss!! >= trade.lastPrice!!) "below your set stop loss" else "beyond your set take profit"

                var builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.notification_title))
                    .setContentText(contentText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                // Dispatch Notification
                with(NotificationManagerCompat.from(context)) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    notify(Constants.NOTIFICATION_REQUEST_ID, builder.build())
                }

                trade.refreshLastNotified()

            }
        }
    }
}