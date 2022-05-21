package com.kako351.healthconnectsample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthDataRequestPermissions
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.HeartRateSeries
import androidx.health.connect.client.records.Steps
import androidx.lifecycle.lifecycleScope
import com.kako351.healthconnectsample.ui.theme.HealthConnectSampleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    val PERMISSIONS =
        setOf(
            Permission.createReadPermission(HeartRateSeries::class),
            Permission.createWritePermission(HeartRateSeries::class),
            Permission.createReadPermission(Steps::class),
            Permission.createWritePermission(Steps::class)
        )

    val requestPermissions =
        registerForActivityResult(HealthDataRequestPermissions()) { granted ->
            if (granted.containsAll(PERMISSIONS)) {
                // Permissions successfully granted
            } else {
                // Lack of required permissions
            }
        }

    var healthConnectClient: HealthConnectClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (HealthConnectClient.isAvailable(this)) {
            // Health Connect is available and installed.
            healthConnectClient = HealthConnectClient.getOrCreate(this)
        } else {
            // ...
            Log.e("HealthConnectClient", "Failed getOrCreate. HealthConnectClient not available.")
        }
        setContent {
            HealthConnectSampleTheme {
                // A surface container using the 'background' color from the theme
                LaunchedEffect("permission") {
                    healthConnectClient?.let {
                        checkPermissionsAndRun(it)
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    fun checkPermissionsAndRun(client: HealthConnectClient) {
        lifecycleScope.launch {
            val granted = client.permissionController.getGrantedPermissions(PERMISSIONS)
            if (granted.containsAll(PERMISSIONS)) {
                // Permissions already granted
            } else {
                requestPermissions.launch(PERMISSIONS)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HealthConnectSampleTheme {
        Greeting("Android")
    }
}