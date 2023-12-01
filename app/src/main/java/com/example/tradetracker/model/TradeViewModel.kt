package com.example.tradetracker.model

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.tradetracker.entity.Trade
import com.example.tradetracker.repository.TradeRepository

class TradeViewModel(application: Application) : AndroidViewModel(application) {
    private val tradeRepository: TradeRepository = TradeRepository(application)

    @RequiresApi(Build.VERSION_CODES.O)
    fun insert(symbol: String, buyPrice: Double, stopLoss: Double, takeProfit: Double, shareValue: Double) {
        tradeRepository.insertRecord(symbol, buyPrice, stopLoss, takeProfit, shareValue)
    }

    fun delete(trade: Trade) {
        tradeRepository.deleteTrade(trade)
    }

    fun closeTrade(trade: Trade) {
        tradeRepository.closeTrade(trade)
    }

    fun update(trade: Trade) {
        tradeRepository.updateRecord(trade)
    }

    fun getTrades(live: Int): List<Trade>
    {
        return tradeRepository.tradeList(live)
    }

    fun getDistinctSymbols() : List<String> {
        return tradeRepository.distinctSymbols()
    }

    fun updateLastPrice(symbol: String, price: Double?) {
        tradeRepository.updateLastPrice(symbol, price)
    }

    fun addTrade(symbol: String, buyPrice: Double, stopLoss: Double, takeProfit: Double, shareValue: Double)
    {
        tradeRepository.insertRecord(symbol, buyPrice, stopLoss, takeProfit, shareValue)
    }

    fun getTradesPastStopOrTake(): List<Trade> {
        return tradeRepository.getTradesPastStopOrTake()
    }
}