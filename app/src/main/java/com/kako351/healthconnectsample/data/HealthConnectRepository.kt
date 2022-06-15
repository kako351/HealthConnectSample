package com.kako351.healthconnectsample.data

import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.BodyTemperature
import androidx.health.connect.client.response.InsertRecordsResponse

interface HealthConnectRepository {
    fun getOrCreateHealthConnectClient(): HealthConnectClient

    suspend fun checkPermissionsAndRun(
        requestPermissions: ActivityResultLauncher<Set<Permission>>,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    )

    suspend fun insertBodyTemperature(
        bodyTemperature: Double,
        onSuccess: (response: InsertRecordsResponse?) -> Unit,
        onFailed: (e: Throwable) -> Unit
    )
}