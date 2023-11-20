package com.example.tradetracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tradetracker.entity.TradeEntity

/**
 * This DAO object validates the SQL at compile-time and associates it with a method
 */
@Dao
interface TradeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TradeEntity::class)
    fun insert(trade: TradeEntity)

    @Query("SELECT * FROM trades WHERE closingDate is null ORDER BY (shareValue*buyPrice)")
    abstract fun findLiveTrades(): List<TradeEntity>

    @Query("SELECT * FROM trades WHERE closingDate not null ORDER BY closingDate")
    abstract fun findHistoricalTrades(): List<TradeEntity>
}

/*

insert new trade
update existing trade

get live trades
get historical trades

 */