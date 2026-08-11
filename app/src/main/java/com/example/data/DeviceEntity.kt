package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val imei: String,
    val model: String,
    val customerName: String,
    val customerPhone: String,
    val status: String, // "LOCKED", "UNLOCKED", "PENDING"
    val lockMessage: String,
    val lockPin: String,
    val remainingBalance: Double,
    val totalInstallments: Int,
    val paidInstallments: Int,
    val createdAt: Long = System.currentTimeMillis()
)
