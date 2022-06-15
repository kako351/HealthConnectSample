package com.kako351.healthconnectsample.data

import android.content.Context
import android.os.Build
import android.system.Os
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.metadata.DataOrigin
import androidx.health.connect.client.metadata.Device
import androidx.health.connect.client.metadata.Metadata
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.BloodPressure
import androidx.health.connect.client.records.BodyTemperature
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import com.kako351.healthconnectsample.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
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


    override suspend fun insertBodyTemperature(
        bodyTemperature: Double,
        onSuccess: (response: InsertRecordsResponse?) -> Unit,
        onFailed: (e: Throwable) -> Unit
    ) {
        val record = listOf(
            BodyTemperature(
                temperatureDegreesCelsius = bodyTemperature,
                measurementLocation = BodyTemperatureMeasurementLocation.ARMPIT,
                time = Instant.now(),
                zoneOffset = ZoneOffset.ofHours(9),
                metadata = Metadata(dataOrigin = DataOrigin(packageName = BuildConfig.APPLICATION_ID), device = Device(manufacturer = Build.MANUFACTURER, model = Build.MODEL, type = Build.TYPE))
            )
        )

        runCatching {
            healthConnectClient?.insertRecords(record)
        }.onSuccess {
            onSuccess(it)
        }.onFailure {
            onFailed(it)
        }
    }

    override suspend fun getBodyTemperature(onSuccess: (response: ReadRecordsResponse<BodyTemperature>?) -> Unit, onFailed: (e: Throwable) -> Unit) {
        val request = ReadRecordsRequest(
            recordType = BodyTemperature::class,
            timeRangeFilter = TimeRangeFilter.Companion.after(Instant.now(Clock.systemDefaultZone()).minusSeconds(60 * 60 * 24)),
            pageSize = 30,
            ascendingOrder = false
        )

        runCatching {
            healthConnectClient?.readRecords(request)
        }.onSuccess {
            onSuccess(it)
        }.onFailure {
            onFailed(it)
        }
    }
}