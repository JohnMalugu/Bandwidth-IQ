package com.jcmlabs.bandwidthiq.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import com.jcmlabs.bandwidthiq.util.hasUsageStatsPermission
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.max

interface SpeedRepository {
    fun monitorSpeed(intervalMs: Long): Flow<Pair<Double, Double>>
    fun isNetworkAvailable(): Boolean
}

class SpeedRepositoryImpl(private val context: Context) : SpeedRepository {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun monitorSpeed(intervalMs: Long): Flow<Pair<Double, Double>> = flow {
        while (true) {
            val downloadSpeed = measureDownloadSpeed()
            val uploadSpeed = measureUploadSpeed()
            emit(Pair(downloadSpeed,uploadSpeed))
            delay(intervalMs)
        }
    }

    override fun isNetworkAvailable(): Boolean {
        val network = safeGetActiveNetwork() ?: return false
        val capabilities = safeGetNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private suspend fun measureDownloadSpeed(): Double {
        if (!isNetworkAvailable()) return 0.0

        return try {
            // Get current UID's traffic stats
            val uid = android.os.Process.myUid()
            val startRxBytes = safeGetUidRxBytes(uid)

            val startTime = System.currentTimeMillis()

            // Measure traffic over a short period
            delay(2000) // 2 second measurement window

            val endRxBytes = safeGetUidRxBytes(uid)
            val endTime = System.currentTimeMillis()

            // Calculate speed
            val bytesReceived = endRxBytes - startRxBytes
            val timeSeconds = (endTime - startTime) / 1000.0

            if (timeSeconds > 0 && bytesReceived > 0) {
                // Convert bytes/second to Mbps
                val speedMbps = (bytesReceived * 8.0) / (timeSeconds * 1_000_000)
                max(speedMbps, 0.1) // Minimum 0.1 Mbps
            } else {
                // Get link speed as fallback
                getLinkSpeed()
            }
        } catch (e: Exception) {
            getLinkSpeed()
        }
    }

    private suspend fun  measureUploadSpeed(): Double {
        if(!isNetworkAvailable()) return 0.0

        return try{
            val uid = android.os.Process.myUid()
            val startTxBytes = safeGetUidTxBytes(uid)
            val startTime = System.currentTimeMillis()

            delay(2000)

            val endTxBytes = safeGetUidTxBytes(uid)
            val endTime = System.currentTimeMillis()

            val bytesTransmitted = endTxBytes -startTxBytes
            val timeSeconds = (endTime - startTime) / 1000.0

            if (timeSeconds > 0 && bytesTransmitted > 0) {
                val speedMbps = (bytesTransmitted * 8.0) / (timeSeconds * 1_000_000)
                max(speedMbps, 0.1)
            } else {
                getUploadLinkSpeed()
            }
        }
        catch (e: Exception){
            getUploadLinkSpeed()
        }
    }

    private fun getLinkSpeed(): Double {
        return try {
            val network = safeGetActiveNetwork() ?: return 1.0
            val capabilities = safeGetNetworkCapabilities(network) ?: return 1.0


            // Get theoretical link speed
            val linkDownstreamBandwidth = capabilities.linkDownstreamBandwidthKbps
            if (linkDownstreamBandwidth > 0) {
                // Convert Kbps to Mbps and estimate actual speed (typically 60-80% of link speed)
                (linkDownstreamBandwidth / 1000.0) * 0.7
            } else {
                // Default estimates based on connection type
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> 25.0
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> 15.0
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> 100.0
                    else -> 5.0
                }
            }
        } catch (e: Exception) {
            5.0 // Default fallback
        }
    }

    private fun getUploadLinkSpeed(): Double {
        return try {
           val network = safeGetActiveNetwork() ?: return 0.5
            val capabilities = safeGetNetworkCapabilities(network) ?: return 0.5


            val linkUpstreamBandwidth = capabilities.linkUpstreamBandwidthKbps
            if (linkUpstreamBandwidth > 0) {
                (linkUpstreamBandwidth / 1000.0) * 0.7
            } else {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> 15.0
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> 10.0
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> 50.0
                    else -> 2.0
                }
            }
        } catch (e: Exception) {
            2.0
        }
    }

    private fun safeGetUidRxBytes(uid: Int): Long {
        return try {
            if (context.hasUsageStatsPermission()) {
                TrafficStats.getUidRxBytes(uid).takeIf { it >= 0 } ?: -1L
            } else {
                -1L
            }
        } catch (e: SecurityException) {
            -1L
        } catch (e: Exception) {
            -1L
        }
    }

    private fun safeGetUidTxBytes(uid: Int): Long {
        return try {
            if (context.hasUsageStatsPermission()) {
                TrafficStats.getUidTxBytes(uid).takeIf { it >= 0 } ?: -1L
            } else {
                -1L
            }
        } catch (e: SecurityException) {
            -1L
        } catch (e: Exception) {
            -1L
        }
    }


    private fun safeGetActiveNetwork() = try {
        connectivityManager.activeNetwork
    } catch (e: SecurityException) {
        null
    }

    private fun safeGetNetworkCapabilities(network: android.net.Network?) = try {
        connectivityManager.getNetworkCapabilities(network)
    } catch (e: SecurityException) {
        null
    }


}
