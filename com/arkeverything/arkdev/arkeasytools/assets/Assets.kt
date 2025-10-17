package com.arkeverything.arkdev.arkeasytools.assets

import androidx.compose.ui.graphics.Color
import com.arkeverything.arkdev.arkeasytools.LiquidScope
import com.arkeverything.arkdev.arkeasytools.Config.LiquidGLconfig

// --------------------- Job ---------------------
sealed class Job {
    data class Click(val action: () -> Unit) : Job()
    data class Drag(val onDrag: (Float, Float) -> Unit) : Job()
    data class Toggle(val onToggle: (Boolean) -> Unit) : Job()
}

// DSL helpers
fun `$click`(action: () -> Unit) = Job.Click(action)
fun `$drag`(onDrag: (Float, Float) -> Unit) = Job.Drag(onDrag)
fun `$toggle`(onToggle: (Boolean) -> Unit) = Job.Toggle(onToggle)

// --------------------- Visual Modifiers ---------------------
fun tint(color: Color = LiquidScope.liquidModifier.tint) {
    LiquidScope.liquidModifier.tint = color
}


fun bleedAmount(value: Float = LiquidScope.liquidModifier.bleedAmount) {
    LiquidScope.liquidModifier.bleedAmount = value
}

fun bleedBlurRadius(value: Float = LiquidScope.liquidModifier.bleedBlurRadius) {
    LiquidScope.liquidModifier.bleedBlurRadius = value
}

fun bleedOpacity(value: Float = LiquidScope.liquidModifier.bleedOpacity) {
    LiquidScope.liquidModifier.bleedOpacity = value
}

fun blurRadius(value: Float = LiquidScope.liquidModifier.blurRadius) {
    LiquidScope.liquidModifier.blurRadius = value
}

fun chromaMultiplier(value: Float = LiquidScope.liquidModifier.chromaMultiplier) {
    LiquidScope.liquidModifier.chromaMultiplier = value
}

fun chromaticAberration(enabled: Boolean = LiquidScope.liquidModifier.chromaticAberration) {
    LiquidScope.liquidModifier.chromaticAberration = enabled
}

fun contrast(value: Float = LiquidScope.liquidModifier.contrast) {
    LiquidScope.liquidModifier.contrast = value
}

fun cornerRadius(value: Float = LiquidScope.liquidModifier.cornerRadius) {
    LiquidScope.liquidModifier.cornerRadius = value
}

fun dispersion(value: Float = LiquidScope.liquidModifier.dispersion) {
    LiquidScope.liquidModifier.dispersion = value
}

fun DP(value: Float = LiquidScope.liquidModifier.DP) {
    LiquidScope.liquidModifier.DP = value
}

fun eccentricFactor(value: Float = LiquidScope.liquidModifier.eccentricFactor) {
    LiquidScope.liquidModifier.eccentricFactor = value
}

fun refractionAmount(value: Float = LiquidScope.liquidModifier.refractionAmount) {
    LiquidScope.liquidModifier.refractionAmount = value
}

fun refractionHeight(value: Float = LiquidScope.liquidModifier.refractionHeight) {
    LiquidScope.liquidModifier.refractionHeight = value
}

fun whitePoint(value: Float = LiquidScope.liquidModifier.whitePoint) {
    LiquidScope.liquidModifier.whitePoint = value
}

