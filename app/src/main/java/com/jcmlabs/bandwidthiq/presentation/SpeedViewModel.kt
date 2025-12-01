package com.jcmlabs.bandwidthiq.presentation


import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jcmlabs.bandwidthiq.data.model.SpeedCategory
import com.jcmlabs.bandwidthiq.data.repository.SpeedRepositoryImpl
import com.jcmlabs.bandwidthiq.domain.MonitorSpeedUseCase
import com.jcmlabs.bandwidthiq.util.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SpeedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SpeedRepositoryImpl(application)
    private val monitorSpeedUseCase = MonitorSpeedUseCase(repository)
    private val notificationHelper = NotificationHelper(application)

    private val _uiState = MutableStateFlow(SpeedUiState())
    val uiState: StateFlow<SpeedUiState> = _uiState.asStateFlow()

    private var monitoringJob: Job? = null
    private var previousCategory: SpeedCategory? = null

    init {
        notificationHelper.createNotificationChannel()
    }

    @SuppressLint("MissingPermission")
    fun startMonitoring() {
        if (_uiState.value.isMonitoring) return

        monitoringJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isMonitoring = true)

            monitorSpeedUseCase(intervalMs = 10000L).collect { speedData ->
                _uiState.value = _uiState.value.copy(
                    currentSpeed = speedData.speedMbps,
                    uploadSpeed = speedData.uploadSpeedMbps,
                    currentCategory = speedData.category,
                    isNetworkAvailable = repository.isNetworkAvailable()
                )

                // Send notification only on category change
                if (previousCategory != null && previousCategory != speedData.category) {
                    notificationHelper.sendCategoryChangeNotification(
                        speed = speedData.speedMbps,
                        category = speedData.category
                    )
                }
                previousCategory = speedData.category
            }
        }
    }

    fun stopMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = null
        previousCategory = null
        _uiState.value = _uiState.value.copy(isMonitoring = false)
    }

    override fun onCleared() {
        super.onCleared()
        stopMonitoring()
    }
}