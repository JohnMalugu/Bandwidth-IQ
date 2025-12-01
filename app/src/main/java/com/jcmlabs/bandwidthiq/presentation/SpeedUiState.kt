package com.jcmlabs.bandwidthiq.presentation

import com.jcmlabs.bandwidthiq.data.model.SpeedCategory


data class SpeedUiState(
    val currentSpeed: Double = 0.0,
    val uploadSpeed: Double = 0.0,
    val currentCategory: SpeedCategory = SpeedCategory.LOW,
    val isMonitoring: Boolean = false,
    val isNetworkAvailable: Boolean = true
)
