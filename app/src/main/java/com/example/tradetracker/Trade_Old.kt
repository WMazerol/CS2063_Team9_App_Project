package com.example.tradetracker

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.Date

//@Entity()
class Trade_Old(private val id: Int = 0,
                   private val symbol: String,
                   private var buyPrice: Double,
                   private var stopLoss: Double,
                   private var takeProfit: Double,
                   private var shareValue: Double) {

    private var lastPrice = 0.00
    private var closingDate: Date? = null

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
    fun getShareValue(): Double {
        return shareValue
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
    fun setShareValue(value: Double) {
        shareValue = value
    }
    fun setLastPrice(value: Double) {
        lastPrice = value
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshLastNotified() {
        lastNotified = LocalDateTime.now()
    }

}