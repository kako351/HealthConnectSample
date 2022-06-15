package com.kako351.healthconnectsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.permission.HealthDataRequestPermissions
import com.kako351.healthconnectsample.ui.theme.HealthConnectSampleTheme
import com.kako351.healthconnectsample.ui.top.TopScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val requestPermissions = registerForActivityResult(HealthDataRequestPermissions()) { granted ->
        viewModel.resultHealthConnectRequestPermissions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthConnectSampleTheme {
                val scaffoldState = rememberScaffoldState()
                // A surface container using the 'background' color from the theme
                LaunchedEffect("permission") {
                    viewModel.checkPermissionsAndRun(requestPermissions)
                }
                val event = viewModel.event.value
                if(event != null) {
                    val message = when(event) {
                        is MainViewModel.Event.InsertSuccess -> "成功"
                        is MainViewModel.Event.InsertFailed -> event.errorMessage
                    }
                    LaunchedEffect("snackbar") {
                        scaffoldState.snackbarHostState.showSnackbar(message)
                    }
                }

                Scaffold(
                    scaffoldState = scaffoldState
                ) {
                    TopScreen(onClick = {
                        viewModel.onClickSaveBodyTemperature(it)
                    })
                }
            }
        }
    }
}
