// LiquidGLConfig.kt
package com.arkeverything.arkdev.arkeasytools.liquidgl.assets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- MAIN UI CONFIG (Buttons, Toggles, Sliders, Menus, etc.) ---
data class LiquidGLConfigData(
    val buttonSize: Dp = 48.dp,
    val buttonPadding: Dp = 16.dp,
    val toggleSize: Dp = 32.dp,
    val sliderWidth: Dp = 200.dp,
    val bottomTabHeight: Dp = 64.dp,
    val bottomTabPadding: Dp = 8.dp,

    // --- MENU CONFIGS ---
    val menuCircleSize: Dp = 56.dp,
    val menuOptionHeight: Dp = 44.dp,
    val menuOptionSpacing: Dp = 6.dp,
    val menuBaseWidth: Dp = 140.dp,
    val menuExtraWidthPerOption: Dp = 8.dp,
    val menuMaxOptions: Int = 10,
    val menuBackgroundTint: Color = Color(0xFFFFFFFF).copy(alpha = 0.06f),
    val menuCornerExpanded: Dp = 12.dp,
    val menuOpenScale: Float = 1.02f,
    val menuOptionTextStyle: TextStyle = TextStyle(fontSize = 14.sp)
)

// --- BOTTOM TABS CONFIG ---
data class LiquidTabsConfigData(
    val tabHeight: Dp = 64.dp,
    val tabPadding: Dp = 4.dp,
    val tabCornerRadius: Dp = 16.dp,
    val tabBlur: Float = 8f,
    val tabLensStrength: Float = 24f,
    val tabDefaultTint: Color = Color(0xFF0088FF),
    val tabInactiveAlpha: Float = 0.6f,
    val containerColorLight: Color = Color(0xFFFAFAFA).copy(alpha = 0.4f),
    val containerColorDark: Color = Color(0xFF121212).copy(alpha = 0.4f)
)

// --- CENTRAL CONFIG CONTROLLER ---
object LiquidGLConfig {

    // Global tint for everything
    var defaultTint by mutableStateOf(Color(0xFF0088FF))

    // --- BUTTON / TOGGLE / SLIDER / MENU CONFIG ---
    private var userConfig: LiquidGLConfigData? = null
    fun set(config: LiquidGLConfigData) { userConfig = config }
    fun get(): LiquidGLConfigData = userConfig ?: LiquidGLConfigData()

    // --- BOTTOM TABS CONFIG ---
    private var userTabsConfig: LiquidTabsConfigData? = null
    fun setTabs(config: LiquidTabsConfigData) { userTabsConfig = config }
    fun getTabs(): LiquidTabsConfigData = userTabsConfig ?: LiquidTabsConfigData()
}

