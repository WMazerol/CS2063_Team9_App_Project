package com.example.tradetracker.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDateTime

@Entity(tableName = "trades") // Represents a SQLite table
 class Trade {

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshLastNotified() {
        lastNotified = formatDateToString(LocalDateTime.now())
    }

    fun formatDateToString(date: LocalDateTime) : String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatStringToDate(date: String) : LocalDateTime {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(date) as LocalDateTime
    }

}
