package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DeviceEntity
import com.example.data.DeviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeviceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DeviceRepository

    val devices: StateFlow<List<DeviceEntity>>
    val lockedCount: StateFlow<Int>
    val totalBalance: StateFlow<Double?>

    init {
        val deviceDao = AppDatabase.getDatabase(application).deviceDao()
        repository = DeviceRepository(deviceDao)

        devices = repository.allDevices
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        lockedCount = repository.lockedCount
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )

        totalBalance = repository.totalBalance
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0.0
            )

        // Seed initial data if empty
        viewModelScope.launch {
            // We can check if list is empty or let user add, but seeding makes the app immediately interactive
            // Let's check via a collect or just insert if needed. Better yet, let's seed 3 sample devices if devices list is empty.
        }
    }

    fun addDevice(
        imei: String,
        model: String,
        customerName: String,
        customerPhone: String,
        status: String,
        lockMessage: String,
        lockPin: String,
        remainingBalance: Double,
        totalInstallments: Int,
        paidInstallments: Int
    ) {
        viewModelScope.launch {
            repository.insert(
                DeviceEntity(
                    imei = imei,
                    model = model,
                    customerName = customerName,
                    customerPhone = customerPhone,
                    status = status,
                    lockMessage = lockMessage,
                    lockPin = lockPin,
                    remainingBalance = remainingBalance,
                    totalInstallments = totalInstallments,
                    paidInstallments = paidInstallments
                )
            )
        }
    }

    fun updateDevice(device: DeviceEntity) {
        viewModelScope.launch {
            repository.update(device)
        }
    }

    fun toggleLockStatus(device: DeviceEntity) {
        viewModelScope.launch {
            val newStatus = if (device.status == "LOCKED") "UNLOCKED" else "LOCKED"
            repository.updateStatus(device.id, newStatus)
        }
    }

    fun deleteDevice(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun seedSampleData() {
        viewModelScope.launch {
            repository.insert(
                DeviceEntity(
                    imei = "354892019283746",
                    model = "iPhone 15 Pro Max",
                    customerName = "Carlos Mendoza",
                    customerPhone = "+52 55 1234 5678",
                    status = "LOCKED",
                    lockMessage = "Dispositivo bloqueado por falta de pago de cuota. Comuníquese al +52 55 8765 4321",
                    lockPin = "9876",
                    remainingBalance = 350.0,
                    totalInstallments = 12,
                    paidInstallments = 8
                )
            )
            repository.insert(
                DeviceEntity(
                    imei = "861920384756102",
                    model = "Samsung Galaxy S24 Ultra",
                    customerName = "Ana Sofía Rivas",
                    customerPhone = "+52 33 9876 5432",
                    status = "UNLOCKED",
                    lockMessage = "Financiamiento al corriente. Gracias por su puntualidad.",
                    lockPin = "1234",
                    remainingBalance = 0.0,
                    totalInstallments = 10,
                    paidInstallments = 10
                )
            )
            repository.insert(
                DeviceEntity(
                    imei = "864201938475619",
                    model = "Xiaomi Redmi Note 13 Pro",
                    customerName = "Luis Alberto Gómez",
                    customerPhone = "+52 81 5555 6666",
                    status = "PENDING",
                    lockMessage = "Cuota próxima a vencer en 3 días.",
                    lockPin = "4321",
                    remainingBalance = 120.0,
                    totalInstallments = 6,
                    paidInstallments = 4
                )
            )
        }
    }
}
