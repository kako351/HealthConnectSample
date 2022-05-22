package com.kako351.healthconnectsample.data

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.BodyTemperature
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HealthConnectRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : HealthConnectRepository {

    private val PERMISSIONS =
        setOf(
            Permission.createReadPermission(BodyTemperature::class),
            Permission.createWritePermission(BodyTemperature::class)
        )

    private var healthConnectClient: HealthConnectClient? = null

    override fun getOrCreateHealthConnectClient(): HealthConnectClient {
        if (healthConnectClient != null) return healthConnectClient as HealthConnectClient

        if (HealthConnectClient.isAvailable(context)) {
            healthConnectClient = HealthConnectClient.getOrCreate(context)
            return healthConnectClient as HealthConnectClient
        } else {
            throw Exception("Failed getOrCreate. HealthConnectClient not available.")
        }
    }

    override suspend fun checkPermissionsAndRun(
        requestPermissions: ActivityResultLauncher<Set<Permission>>,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) {
        val granted = getOrCreateHealthConnectClient().permissionController.getGrantedPermissions(PERMISSIONS)
        if (granted.containsAll(PERMISSIONS)) {
            // Permissions already granted
            onSuccess()
        } else {
            requestPermissions.launch(PERMISSIONS)
        }
    }
}