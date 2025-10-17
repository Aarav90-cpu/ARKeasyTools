package com.arkeverything.arkdev.arkeasytools.Config

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// In LiquidGLconfig.kt

object LiquidGLconfig {
    // Common LiquidGlass defaults
    val defaultTint: Color = Color.Unspecified
    val defaultOpacity: Float = 1f
    val defaultBleedAmount: Float = 0.2f
    val defaultBleedBlurRadius: Float = 8f
    val defaultBleedOpacity: Float = 0.5f
    val defaultBlurRadius: Float = 16f
    val defaultChromaMultiplier: Float = 1f
    val defaultChromaticAberration: Boolean = false
    val defaultContrast: Float = 1f
    val defaultCornerRadius: Float = 12f
    val defaultDispersion: Float = 0f
    val defaultDP: Float = 1f
    val defaultEccentricFactor: Float = 0f
    val defaultRefractionAmount: Float = 0f
    val defaultRefractionHeight: Float = 0f
    val defaultWhitePoint: Float = 1f

    // Button defaults
    val buttonHeight = 48.dp
    val buttonHorizontalPadding = 16.dp
    val buttonTint: Color = Color.Unspecified
    val buttonSurfaceColor: Color = Color.Unspecified
    val buttonIsInteractive: Boolean = true

    // Slider defaults
    val sliderHeight = 6.dp
    val sliderThumbWidth = 40.dp
    val sliderThumbHeight = 24.dp
    val sliderTrackColor: Color = Color(0xFF787878).copy(alpha = 0.2f)
    val sliderAccentColor: Color = Color(0xFF0088FF)
    val sliderVisibilityThreshold = 0.001f
    val sliderInitialScale = 1f
    val sliderPressedScale = 1.5f
    val sliderBlur = 8f
    val sliderRefractionAmount = 10f
    val sliderRefractionHeight = 14f

    // Toggle defaults
    val toggleWidth = 64.dp
    val toggleHeight = 28.dp
    val toggleAccentColor = Color(0xFF34C759)
    val toggleTrackColor = Color(0xFF787878).copy(alpha = 0.2f)

    // BottomTabs defaults
    val bottomTabHeight = 56.dp
    val bottomTabContainerHeight = 64.dp
    val bottomTabAccentColor = Color(0xFF0088FF)
    val bottomTabContainerColor = Color(0xFFFAFAFA).copy(alpha = 0.4f)
    val bottomTabPadding = 4.dp
}
