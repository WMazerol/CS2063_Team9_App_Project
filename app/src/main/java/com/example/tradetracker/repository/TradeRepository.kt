package com.example.tradetracker.repository

import com.example.tradetracker.db.AppDatabase.Companion.getDatabase
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tradetracker.dao.TradeDao
import com.example.tradetracker.db.AppDatabase
import com.example.tradetracker.entity.Trade
import java.time.LocalDateTime
import java.util.Calendar
import java.util.concurrent.Future
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

class TradeRepository(application: Application) {
    private val tradeDao: TradeDao? = getDatabase(application).tradeDao()

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertRecord(symbol: String, buyPrice: Double, stopLoss: Double, takeProfit: Double, shareValue: Double) {
        val trade = Trade()
        trade.symbol = symbol
        trade.buyPrice = buyPrice
        trade.stopLoss = stopLoss
        trade.takeProfit = takeProfit
        trade.shareValue = shareValue
        trade.lastNotified = trade.formatDateToString(LocalDateTime.now())
        insert(trade)
    }

    fun closeTrade(trade: Trade) {
        val date = Calendar.YEAR.toString() + "-" + Calendar.MONTH.toString() + "-" + Calendar.DAY_OF_MONTH.toString()
        trade.closingDate = date
        insert(trade)
    }

    fun updateRecord(trade: Trade) {
        insert(trade)
    }

    private fun insert(trade: Trade) {
        // Using a Runnable thread object as there are no return values
        AppDatabase.databaseWriterExecutor.execute { tradeDao!!.insert(trade) }
    }

    fun deleteTrade(trade: Trade) {
        delete(trade.id)
    }

    private fun delete(id: Int) {
        // Using a Runnable thread object as there are no return values
        AppDatabase.databaseWriterExecutor.execute { tradeDao!!.deleteTrade(id) }
    }

    fun distinctSymbols() : List<String>{
        val dataReadFuture: Future<List<String>>? = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                return@Callable tradeDao!!.getDistinctSymbols()
            })

        while (!dataReadFuture!!.isDone) {// Simulating another task
            TimeUnit.MILLISECONDS.sleep(10)
        }

        return dataReadFuture.get()
    }

    fun updateLastPrice(symbol: String, price: Double) {
        AppDatabase.databaseWriterExecutor.execute {tradeDao!!.updateLastPrice(symbol, price)}
    }

    fun tradeList(live: Int): List<Trade> {
        // Using a Callable thread object as there are return values
        val dataReadFuture: Future<List<Trade>>? = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                if(live == 1)
                {
                    return@Callable tradeDao!!.findLiveTrades()
                }
                else
                {
                    return@Callable tradeDao!!.findHistoricalTrades()
                }
            })

        while (!dataReadFuture!!.isDone) {// Simulating another task
            TimeUnit.MILLISECONDS.sleep(10)
        }

        return dataReadFuture.get()// as List<Item>
    }

    fun getTradesPastStopOrTake(): List<Trade> {
        val dataReadFuture: Future<List<Trade>>? = AppDatabase.databaseWriterExecutor.submit(
            Callable {
                return@Callable tradeDao!!.getTradesPastStopOrTake()
            })

        while (!dataReadFuture!!.isDone) {// Simulating another task
            TimeUnit.MILLISECONDS.sleep(10)
        }

        return dataReadFuture.get()
    }
}