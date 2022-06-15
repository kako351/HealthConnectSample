package com.kako351.healthconnectsample

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.BodyTemperature
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

    private val _list = mutableStateOf<List<BodyTemperature>>(emptyList())
    val list: State<List<BodyTemperature>> get() = _list

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
                    readBodyTemperatures()
                },
                onFailed = {
                    Log.e("MainViewModel", "insertBodyTemperature Failed message=${it.message}")
                    _event.value = Event.InsertFailed("体温の書き込みに失敗しました。パーミッションが許可されているか確認してください")
                }
            )
        }
    }

    fun readBodyTemperatures() {
        viewModelScope.launch {
            healthConnectRepository.getBodyTemperature(
                onSuccess = {
                    Log.d("MainViewModel", "readBodyTemperatures Success")
                    _list.value = it?.records ?: emptyList()
                },
                onFailed = {
                    Log.e("MainViewModel", "readBodyTemperatures Failed message=${it.message}")
                    _event.value = Event.InsertFailed("体温データの取得に失敗しました。パーミッションが許可されているか確認してください")
                }
            )
        }
    }
}