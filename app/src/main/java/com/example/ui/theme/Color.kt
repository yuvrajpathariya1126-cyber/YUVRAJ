package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val InstaViolet = Color(0xFF515BD4)
val InstaPurple = Color(0xFF8134AF)
val InstaPink = Color(0xFFDD2A7B)
val InstaCoral = Color(0xFFF58529)
val InstaGold = Color(0xFFFEDA77)

val DarkBg = Color(0xFF0C0B12)
val DarkSurface = Color(0xFF151420)
val DarkCard = Color(0xFF1D1C2D)
val DarkCardBorder = Color(0xFF2C2A42)

val LightBg = Color(0xFFF7F8FC)
val LightSurface = Color(0xFFFFFFFF)
val LightCardBorder = Color(0xFFE2E4EB)

val MetricGreen = Color(0xFF00C853)
val MetricOrange = Color(0xFFFF9100)
val MetricBlue = Color(0xFF2979FF)
val MetricPurple = Color(0xFFAA00FF)

val InstaGradient = Brush.linearGradient(
    colors = listOf(
        InstaPurple,
        InstaPink,
        InstaCoral
    )
)

val InstaGoldGradient = Brush.linearGradient(
    colors = listOf(
        InstaCoral,
        InstaGold
    )
)
