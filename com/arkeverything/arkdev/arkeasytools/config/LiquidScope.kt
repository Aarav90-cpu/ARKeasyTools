package com.arkeverything.arkdev.arkeasytools.config

import com.arkeverything.arkdev.arkeasytools.Config.LiquidGLconfig
import androidx.compose.ui.graphics.Color


object LiquidScope {
    val liquidModifier = LiquidModifier()
}

// --------------------- Modifier Container ---------------------
class LiquidModifier {
    var tint: Color = LiquidGLconfig.defaultTint
    var opacity: Float = LiquidGLconfig.defaultOpacity
    var bleedAmount: Float = LiquidGLconfig.defaultBleedAmount
    var bleedBlurRadius: Float = LiquidGLconfig.defaultBleedBlurRadius
    var bleedOpacity: Float = LiquidGLconfig.defaultBleedOpacity
    var blurRadius: Float = LiquidGLconfig.defaultBlurRadius
    var chromaMultiplier: Float = LiquidGLconfig.defaultChromaMultiplier
    var chromaticAberration: Boolean = LiquidGLconfig.defaultChromaticAberration
    var contrast: Float = LiquidGLconfig.defaultContrast
    var cornerRadius: Float = LiquidGLconfig.defaultCornerRadius
    var dispersion: Float = LiquidGLconfig.defaultDispersion
    var DP: Float = LiquidGLconfig.defaultDP
    var eccentricFactor: Float = LiquidGLconfig.defaultEccentricFactor
    var refractionAmount: Float = LiquidGLconfig.defaultRefractionAmount
    var refractionHeight: Float = LiquidGLconfig.defaultRefractionHeight
    var whitePoint: Float = LiquidGLconfig.defaultWhitePoint
}

// --------------------- Singleton LiquidScope ---------------------

