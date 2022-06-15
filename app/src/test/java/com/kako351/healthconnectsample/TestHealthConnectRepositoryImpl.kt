package com.kako351.healthconnectsample

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.Permission
import com.kako351.healthconnectsample.data.HealthConnectRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TestHealthConnectRepositoryImpl @Inject constructor(@ApplicationContext val context: Context): HealthConnectRepository {
    override fun getOrCreateHealthConnectClient(): HealthConnectClient {
        return HealthConnectClient.getOrCreate(context)
    }

    override suspend fun checkPermissionsAndRun(
        requestPermissions: ActivityResultLauncher<Set<Permission>>,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) {
        // noop
    }
}