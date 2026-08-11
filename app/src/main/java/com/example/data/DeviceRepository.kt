package com.example.data

import kotlinx.coroutines.flow.Flow

class DeviceRepository(private val deviceDao: DeviceDao) {
    val allDevices: Flow<List<DeviceEntity>> = deviceDao.getAllDevices()
    val lockedCount: Flow<Int> = deviceDao.getLockedCount()
    val totalBalance: Flow<Double?> = deviceDao.getTotalRemainingBalance()

    fun getDeviceById(id: Long): Flow<DeviceEntity?> = deviceDao.getDeviceById(id)

    suspend fun insert(device: DeviceEntity): Long = deviceDao.insertDevice(device)

    suspend fun update(device: DeviceEntity) = deviceDao.updateDevice(device)

    suspend fun delete(id: Long) = deviceDao.deleteDevice(id)

    suspend fun updateStatus(id: Long, status: String) = deviceDao.updateStatus(id, status)
}
