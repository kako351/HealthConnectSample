package com.kako351.healthconnectsample

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.permission.Permission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kako351.healthconnectsample.data.HealthConnectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val healthConnectRepository: HealthConnectRepository
) : ViewModel() {
    fun resultHealthConnectRequestPermissions() {
        Log.i("MainViewModel", "registerForActivityResult callback")
    }
    fun checkPermissionsAndRun(requestPermissions: ActivityResultLauncher<Set<Permission>>) = viewModelScope.launch {
        healthConnectRepository.checkPermissionsAndRun(
            requestPermissions,
            onSuccess = {
                Log.i("MainViewModel", "checkPermissionsAndRun Success")
            },
            onFailed = {
                Log.e("MainViewModel", "checkPermissionsAndRun Failed")
            }
        )
    }
}