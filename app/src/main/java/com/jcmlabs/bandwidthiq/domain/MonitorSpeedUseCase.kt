package com.jcmlabs.bandwidthiq.domain


import com.jcmlabs.bandwidthiq.data.model.SpeedCategory
import com.jcmlabs.bandwidthiq.data.model.SpeedData
import com.jcmlabs.bandwidthiq.data.repository.SpeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MonitorSpeedUseCase(private val repository: SpeedRepository) {
    operator fun invoke(intervalMs: Long = 10000L): Flow<SpeedData> {
        return repository.monitorSpeed(intervalMs).map { (downloadSpeed, uploadSpeed) ->
            SpeedData(
                speedMbps = downloadSpeed,
                uploadSpeedMbps = uploadSpeed,
                category = SpeedCategory.fromSpeed(downloadSpeed)
            )
        }
    }
}