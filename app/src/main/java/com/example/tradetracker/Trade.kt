package com.example.tradetracker

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Trade(private val symbol: String,
            private var buyPrice: Double,
            private var stopLoss: Double,
            private var takeProfit: Double,
            private var shareQuantity: Double) {

    private var lastPrice = 0.00

    @RequiresApi(Build.VERSION_CODES.O)
    private var lastNotified = LocalDateTime.now()

    //Getters
    fun getSymbol(): String {
        return symbol.uppercase()
    }
    fun getBuyPrice(): Double {
        return buyPrice
    }
    fun getStopLoss(): Double {
        return stopLoss
    }
    fun getTakeProfit(): Double {
        return takeProfit
    }
    fun getShareQuantity(): Double {
        return shareQuantity
    }
    fun getLastPrice(): Double {
        return lastPrice
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getLastNotified(): LocalDateTime {
        return lastNotified
    }

    //Setters
    fun setBuyPrice(value: Double) {
        buyPrice = value
    }
    fun setStopLoss(value: Double) {
        stopLoss = value
    }
    fun setTakeProfit(value: Double) {
        takeProfit = value
    }
    fun setShareQuantity(value: Double) {
        shareQuantity = value
    }
    fun setLastPrice(value: Double) {
        lastPrice = value
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshLastNotified() {
        lastNotified = LocalDateTime.now()
    }

}