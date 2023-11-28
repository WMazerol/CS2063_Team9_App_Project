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

    @Query("SELECT * FROM trades WHERE closingDate not null ORDER BY closingDate")
    abstract fun findHistoricalTrades(): List<Trade>

    @Query("DELETE FROM trades WHERE id is (:id)")
    abstract fun deleteTrade(id: Int)
}

/*

insert new trade
update existing trade

get live trades
get historical trades

 */