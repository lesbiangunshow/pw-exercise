package com.abbisea.pwexercise.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_inspections")
data class PendingInspection(
    val inspectionId: Int,
    val inspectionName: String,
    val location: String,
    val answers: List<Int?> // answer choice can be 0-100, null if N/A
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}