package com.kako351.healthconnectsample

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
    sealed class Event {
        object InsertSuccess: Event()
        data class InsertFailed(val errorMessage: String): Event()
    }

    private val _event = mutableStateOf<Event?>(null)
    val event: State<Event?> get() = _event

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

    fun onClickSaveBodyTemperature(value: Double) {
        Log.i("MainViewModel", "save value=$value")
        viewModelScope.launch {
            healthConnectRepository.insertBodyTemperature(
                value,
                onSuccess = {
                    Log.d("MainViewModel", "insertBodyTemperature Success")
                    _event.value = Event.InsertSuccess
                },
                onFailed = {
                    Log.e("MainViewModel", "insertBodyTemperature Failed message=${it.message}")
                    _event.value = Event.InsertFailed("体温の書き込みに失敗しました。パーミッションが許可されているか確認してください")
                }
            )
        }
    }
}