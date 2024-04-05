package com.example.secure

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DrugItem::class], version = 1, exportSchema = false)
abstract class DrugItemDatabase : RoomDatabase() {

    abstract fun drugItemDao(): DrugItemDao

    companion object {
        @Volatile
        private var INSTANCE: DrugItemDatabase? = null

        fun getDatabase(context: Context): DrugItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrugItemDatabase::class.java,
                    "drug_item_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}