package com.example.secure

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface DrugItemDao {
    @Query("SELECT * FROM drug_item_table ORDER BY id ASC")
    fun allDrugItems(): Flow<List<DrugItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrugItem(drugItem: DrugItem)

    @Update
    suspend fun updateDrugItem(drugItem: DrugItem)

    @Delete
    suspend fun deleteDrugItem(drugItem: DrugItem)
}