package com.example.tradetracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tradetracker.entity.TradeEntity
import com.example.tradetracker.repository.TradeRepository

class TradeViewModel(application: Application) : AndroidViewModel(application)
{
    private val tradeRepository: TradeRepository = TradeRepository(application)

    fun getTrades(live: Int): List<TradeEntity>
    {
        return tradeRepository.tradeList(live)
    }

    fun addTrade(symbol: String, buyPrice: Double, stopLoss: Double, takeProfit: Double, shareValue: Double)
    {
        tradeRepository.insertRecord(symbol, buyPrice, stopLoss, takeProfit, shareValue)
    }
}