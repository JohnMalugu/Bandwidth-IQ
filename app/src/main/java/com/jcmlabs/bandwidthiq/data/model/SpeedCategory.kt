package com.jcmlabs.bandwidthiq.data.model


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class SpeedCategory(
    val displayName: String,
    val minSpeed: Double,
    val maxSpeed: Double,
    val color: Color,
    val gradient: List<Color>,
    val icon: ImageVector,
    val activities: List<String>
) {
    LOW(
        displayName = "Low Speed",
        minSpeed = 0.0,
        maxSpeed = 5.0,
        color = Color(0xFFE57373),
        gradient = listOf(Color(0xFFEF5350), Color(0xFFE57373)),
        icon = Icons.Default.Warning,
        activities = listOf(
            "✓ Basic web browsing",
            "✓ Email checking",
            "✓ Text messaging",
            "✓ Light social media",
            "⚠ Streaming not recommended"
        )
    ),
    MEDIUM(
        displayName = "Medium Speed",
        minSpeed = 5.0,
        maxSpeed = 25.0,
        color = Color(0xFFFFB74D),
        gradient = listOf(Color(0xFFFFA726), Color(0xFFFFB74D)),
        icon = Icons.Default.Check,
        activities = listOf(
            "✓ HD video streaming (720p)",
            "✓ Video calls",
            "✓ Online gaming (casual)",
            "✓ Music streaming",
            "✓ Regular browsing"
        )
    ),
    HIGH(
        displayName = "High Speed",
        minSpeed = 25.0,
        maxSpeed = Double.MAX_VALUE,
        color = Color(0xFF66BB6A),
        gradient = listOf(Color(0xFF4CAF50), Color(0xFF66BB6A)),
        icon = Icons.Default.Star,
        activities = listOf(
            "✓ 4K Ultra HD streaming",
            "✓ Multiple device streaming",
            "✓ Large file downloads",
            "✓ HD video calls",
            "✓ Competitive online gaming",
            "✓ Cloud gaming"
        )
    );

    companion object {
        fun fromSpeed(speed: Double): SpeedCategory {
            return entries.firstOrNull { speed >= it.minSpeed && speed < it.maxSpeed } ?: HIGH
        }
    }
}