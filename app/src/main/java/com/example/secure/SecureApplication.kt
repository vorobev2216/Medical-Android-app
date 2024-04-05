package com.example.secure

import android.app.Application

class SecureApplication: Application() {
    private val database by lazy { DrugItemDatabase.getDatabase(this) }
    val repository by lazy { DrudItemRepository(database.drugItemDao()) }
}