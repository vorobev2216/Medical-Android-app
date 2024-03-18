package com.example.secure

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class DrugItem(
    var name: String,
    var desc: String,
    var dueTime: LocalTime?,
    var completedDate: LocalDate?,
    var id: UUID = UUID.randomUUID()
) {
    fun isCompleted(): Boolean {
        return completedDate != null
    }

    fun cardColor(context: Context) {
        if (isCompleted()) {
            grey(context)
        } else {
            main_blue(context)
        }
    }
    fun imageResource(): Int = if(isCompleted()) R.drawable.checked_24 else R.drawable.unchecked_24
    fun grey(context: Context) {
        ContextCompat.getColor(context, R.color.grey_checked)
    }


    fun main_blue(context: Context) {
        ContextCompat.getColor(context, R.color.main_blue)
    }
}