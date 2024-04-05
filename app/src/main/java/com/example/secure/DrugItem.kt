package com.example.secure

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Entity(tableName = "drug_item_table")
data class DrugItem(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "desc") var desc: String,
    @ColumnInfo(name = "dueTime") var dueTimeString: String?,
    @ColumnInfo(name = "completedDate") var completedDateString: String?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) {

    private fun completedDate(): LocalDate? = if (completedDateString == null) null else LocalDate.parse(completedDateString, dateFormatter)
    fun dueTime(): LocalTime? = if (dueTimeString == null) null else LocalTime.parse(dueTimeString, timeFormatter)

    fun isCompleted(): Boolean {
        return completedDate() != null
    }

    fun cardColor(context: Context) {
        if (isCompleted()) {
            grey(context)
        } else {
            main_blue(context)
        }
    }

    fun imageResource(): Int = if (isCompleted()) R.drawable.checked_24 else R.drawable.unchecked_24
    fun grey(context: Context) {
        ContextCompat.getColor(context, R.color.grey_checked)
    }


    fun main_blue(context: Context) {
        ContextCompat.getColor(context, R.color.main_blue)
    }

    companion object {
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_TIME
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE
    }
}