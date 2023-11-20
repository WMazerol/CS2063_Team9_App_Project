package com.example.tradetracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tradetracker.dao.TradeDao
import kotlin.jvm.Volatile
import androidx.room.Room
import com.example.tradetracker.entity.TradeEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Database layer in top of the SQLite database
 */
@Database(entities = [TradeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tradeDao(): TradeDao?

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val NUMBER_OF_THREADS = 4

        // Define an ExecutorService with a fixed thread pool which is used to run database operations asynchronously on a background thread
        val databaseWriterExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        // Singleton access to the database
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "app_database")
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
