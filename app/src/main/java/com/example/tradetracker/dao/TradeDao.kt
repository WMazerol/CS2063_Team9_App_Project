package com.example.tradetracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tradetracker.entity.Trade

/**
 * This DAO object validates the SQL at compile-time and associates it with a method
 */
@Dao
interface TradeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = Trade::class)
    fun insert(trade: Trade)

    @Query("SELECT * FROM trades WHERE closingDate is null ORDER BY (shareValue*buyPrice)")
    abstract fun findLiveTrades(): List<Trade>

    @Query("SELECT * FROM trades WHERE closingDate not null ORDER BY closingDate DESC")
    abstract fun findHistoricalTrades(): List<Trade>

    @Query("DELETE FROM trades WHERE id is (:id)")
    abstract fun deleteTrade(id: Int)

    @Query("SELECT DISTINCT symbol FROM trades WHERE closingDate is null AND closingDate is null")
    abstract fun getDistinctSymbols(): List<String>

    @Query("UPDATE trades SET lastPrice = (:price) WHERE symbol is (:symbol) AND closingDate is null")
    abstract fun updateLastPrice(symbol: String, price: Double?)

    @Query("SELECT * FROM trades WHERE (lastPrice > takeProfit OR lastPrice < stopLoss) AND closingDate is null")
    abstract fun getTradesPastStopOrTake(): List<Trade>

    @Query("SELECT DISTINCT isCrypto FROM trades WHERE symbol = (:symbol)")
    abstract fun symbolIsCrypto(symbol: String): Boolean
}

/*

insert new trade
update existing trade

get live trades
get historical trades

 */