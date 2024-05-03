package com.example.secure.Room

import android.app.Application
import com.example.secure.Room.DrudItemRepository
import com.example.secure.Room.DrugItemDatabase

class SecureApplication: Application() {
    private val database by lazy { DrugItemDatabase.getDatabase(this) }
    val repository by lazy { DrudItemRepository(database.drugItemDao()) }
}