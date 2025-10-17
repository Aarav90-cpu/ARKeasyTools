package com.arkeverything.arkdev.arkeasytools.backdrops

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlin.math.max

/**
 * Represents a single visual layer for a backdrop.
 */
data class BackdropLayer(
    val blurRadius: Float = 0f,
    val contrast: Float = 1f,
    val dispersion: Float = 0f,
    val tint: Color = Color.Unspecified
)

/**
 * Holds multiple BackdropLayer objects as a stacked layered backdrop.
 */
class LayeredBackdrop(
    val layers: List<BackdropLayer>
)

/**
 * Composable helper to remember a default layered backdrop.
 * Creates two layers by default, customizable via parameters.
 */
@Composable
fun rememberLayeredBackdrop(
    blurRadius: Float = 20f,
    contrast: Float = 1f,
    dispersion: Float = 0.1f,
    tint: Color = Color.Unspecified
): LayeredBackdrop {
    return remember {
        LayeredBackdrop(
            layers = listOf(
                BackdropLayer(
                    blurRadius = blurRadius,
                    contrast = contrast,
                    dispersion = dispersion,
                    tint = tint
                ),
                BackdropLayer(
                    blurRadius = max(blurRadius * 1.5f, 30f),
                    contrast = contrast * 0.95f,
                    dispersion = dispersion * 1.2f,
                    tint = tint
                )
            )
        )
    }
}
