package com.example.tradetracker

import android.Manifest
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.StrictMode
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tradetracker.repository.TradeRepository
import java.time.Duration
import java.time.LocalDateTime

private const val NOTIFICATION_TIME_DELAY_HOURS = 0 //Should be 2 (for 2 hours) in production, 0 is for demo
private var lastMultiNotification: LocalDateTime? = null

class AlarmReceiver : BroadcastReceiver() {

    //private val tradeRepository: TradeRepository = TradeRepository(MainActivity().application)
    private val apiController: APIController = APIController()
    val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val tradeRepository = TradeRepository(context.applicationContext as Application)
        StrictMode.setThreadPolicy(policy)

        println("Alarm Received")

        for(symbol in tradeRepository.distinctSymbols()) {
            val price: Double?

            if(tradeRepository.symbolIsCrypto(symbol)) {
                price = apiController.getBinancePrice(symbol)

                if (price != 0.0) // Protects against API errors
                    tradeRepository.updateLastPrice(symbol, price)

            } else {
                price = apiController.getFinnhubPrice(symbol)

                if(price != 0.0) // Protects against API errors
                    tradeRepository.updateLastPrice(symbol, price)
            }
        }

        val contentText = "Multiple trades require your attention!"
        val openIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_IMMUTABLE)

        val tradesPastStopOrTake = tradeRepository.getTradesPastStopOrTake()

        if(tradesPastStopOrTake.size == 1) {
            val trade = tradesPastStopOrTake[0]
            if (trade.lastNotified == null)
                trade.refreshLastNotified()

            if (trade.lastNotified == null || Duration.between(
                    trade.formatStringToDate(trade.lastNotified!!),
                    LocalDateTime.now()
                ).toHours() >= NOTIFICATION_TIME_DELAY_HOURS
            ) {
                val contentText = trade.symbol + " has gone " +
                        if (trade.stopLoss!! >= trade.lastPrice!!) "below your set stop loss" else "beyond your set take profit"

                trade.refreshLastNotified()
            }
        } else {
            if(lastMultiNotification != null && Duration.between(lastMultiNotification,
                LocalDateTime.now()).toHours() < NOTIFICATION_TIME_DELAY_HOURS)
                return

            lastMultiNotification = LocalDateTime.now()
        }



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
    }
}