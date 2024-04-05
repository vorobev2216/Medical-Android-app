package com.example.secure

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class DrudItemRepository(private val drugItemDao: DrugItemDao) {

    val allDrugItems: Flow<List<DrugItem>> = drugItemDao.allDrugItems()

    @WorkerThread
    suspend fun insertDrugItem(drugItem: DrugItem)
    {
        drugItemDao.insertDrugItem(drugItem)
    }

    @WorkerThread
    suspend fun updateDrugItem(drugItem: DrugItem)
    {
        drugItemDao.updateDrugItem(drugItem)
    }
}