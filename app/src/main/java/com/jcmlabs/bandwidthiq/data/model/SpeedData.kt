package com.jcmlabs.bandwidthiq.data.model

data class SpeedData(
    val speedMbps: Double,
    val uploadSpeedMbps: Double,
    val category: SpeedCategory,
    val timestamp: Long = System.currentTimeMillis()
)