package com.jcmlabs.bandwidthiq

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jcmlabs.bandwidthiq.presentation.SpeedViewModel
import com.jcmlabs.bandwidthiq.presentation.ui.component.BandwidthIQScreen
import com.jcmlabs.bandwidthiq.ui.theme.BandwidthIQTheme
import com.jcmlabs.bandwidthiq.util.hasUsageStatsPermission

class MainActivity : ComponentActivity() {

    private val viewModel: SpeedViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) viewModel.startMonitoring()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BandwidthIQTheme {
                val uiState by viewModel.uiState.collectAsState()

                BandwidthIQScreen(
                    uiState = uiState,
                    onToggleMonitoring = {
                        if (uiState.isMonitoring) {
                            viewModel.stopMonitoring()
                        } else {
                            if (!hasUsageStatsPermission()) {
                                openUsageAccessSettings()
                            } else {
                                requestNotificationPermissionAndStart()
                            }
                        }
                    }
                )
            }
        }
    }

    private fun requestNotificationPermissionAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.startMonitoring()
        }
    }

    private fun openUsageAccessSettings() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    }
}
