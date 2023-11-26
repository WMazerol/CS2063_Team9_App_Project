package com.example.tradetracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trades") // Represents a SQLite table
 class TradeEntity {

    @PrimaryKey(autoGenerate = true) var id: Int = 0
        get() = field

    @ColumnInfo
    var symbol: String? = null
        get() = field
        set(value) {
            field = value
        }

    @ColumnInfo
    var buyPrice: Double? = null
        get() = field
        set(value) {
            field = value
        }

    @ColumnInfo
    var stopLoss: Double? = null
        get() = field
        set(value) {
            field = value
        }

    @ColumnInfo
    var takeProfit: Double? = null
        get() = field
        set(value) {
            field = value
        }

    @ColumnInfo
    var shareValue: Double? = null
        get() = field
        set(value) {
            field = value
        }

    @ColumnInfo
    var lastPrice: Double? = null
        get() = field
        set(value) {
            field = value
        }

    @ColumnInfo
    var closingDate: String? = null
        get() = field
        set(value) {
            field = value
        }

    @ColumnInfo
    var lastNotified: String? = null
        get() = field
        set(value) {
            field = value
        }
}
