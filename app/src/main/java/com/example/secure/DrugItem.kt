package com.example.secure

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
}