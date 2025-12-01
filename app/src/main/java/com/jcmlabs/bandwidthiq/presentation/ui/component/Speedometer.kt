package com.jcmlabs.bandwidthiq.presentation.ui.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward


@Composable
fun SpeedIndicator(
    label: String,
    speed: Double,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        icon()
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${"%.1f".format(speed)} Mbps",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun LiveSpeedometer(
    downloadSpeed: Double,
    uploadSpeed: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Download Speed
        SpeedIndicator(
            label = "Download",
            speed = downloadSpeed,
            icon = {
                Icon(
                    Icons.Default.ArrowDownward,
                    contentDescription = "Download",
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF4CAF50) // Green
                )
            }
        )

        // Divider
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(80.dp)
                .background(Color.White.copy(alpha = 0.3f))
        )

        // Upload Speed
        SpeedIndicator(
            label = "Upload",
            speed = uploadSpeed,
            icon = {
                Icon(
                    Icons.Default.ArrowUpward,
                    contentDescription = "Upload",
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF2196F3) // Blue
                )
            }
        )
    }
}