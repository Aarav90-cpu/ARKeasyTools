package com.arkeverything.arkdev.arkeasytools

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import com.arkeverything.arkdev.arkeasytools.Config.LiquidGLconfig
import com.arkeverything.arkdev.arkeasytools.backdrops.DummyBackdrop
import com.arkeverything.arkdev.arkeasytools.backdrops.LayeredBackdrop
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.*
import com.kyant.capsule.ContinuousCapsule
import kotlinx.coroutines.CoroutineScope
import com.arkeverything.arkdev.arkeasytools.assets.*

/**
 * LiquidScope holds global modifier properties for Liquid components
 */
object LiquidScope {
    val liquidModifier = LiquidModifier()
}

class LiquidModifier {
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
    var tint: Color = LiquidGLconfig.defaultTint
    var whitePoint: Float = LiquidGLconfig.defaultWhitePoint
}

/**
 * Central LiquidGL object — bridges Backdrops, Config, Assets, and all components
 */
object LiquidGL {

    // -------------------
    // GLOBAL SCOPE + CONFIG
    // -------------------
    val scope: CoroutineScope
        @Composable get() = rememberCoroutineScope()

    @Composable
    fun Density() = LocalDensity.current

    // -------------------
    // BACKDROP ACCESS
    // -------------------

    @Composable
    fun BackdropLayered(
        modifier: Modifier = Modifier,
        color: Color? = null,
        blurRadius: Float = LiquidScope.liquidModifier.blurRadius,
        refractionAmount: Float = LiquidScope.liquidModifier.refractionAmount,
        refractionHeight: Float = LiquidScope.liquidModifier.refractionHeight,
        tint: Color = LiquidScope.liquidModifier.tint,
        content: @Composable () -> Unit
    ) {
        val layeredBackdrop = remember { LayeredBackdrop(layers = listOf()) }

        BoxWithConstraints(modifier = modifier) {
            val width = maxWidth
            val height = maxHeight

            Box(
                Modifier.drawBackdrop(
                    backdrop = layeredBackdrop as Backdrop,
                    shape = { ContinuousCapsule },
                    effects = {
                        vibrancy()
                        blur(blurRadius)
                        lens(refractionAmount, refractionHeight)
                        if (LiquidScope.liquidModifier.chromaticAberration) chromaticAberration()
                    },
                    onDrawSurface = {
                        if (tint != Color.Unspecified) drawRect(tint)
                    }
                )
            ) {
                content()
            }
        }
    }

    @Composable
    fun DummyBackdropLayered(
        modifier: Modifier = Modifier,
        color: Color? = null,
        blurRadius: Float? = null,
        tint: Color? = null,
        content: @Composable () -> Unit = {}
    ) {
        val layeredBackdrop = remember { LayeredBackdrop(layers = listOf()) }

        BoxWithConstraints(modifier = modifier) {
            val width = maxWidth
            val height = maxHeight

            Box(
                Modifier.drawBackdrop(
                    backdrop = layeredBackdrop as Backdrop,
                    shape = { ContinuousCapsule },
                    effects = {
                        vibrancy()
                        blur(blurRadius ?: LiquidGLconfig.defaultBlurRadius)
                        lens(
                            LiquidScope.liquidModifier.refractionAmount,
                            LiquidScope.liquidModifier.refractionHeight
                        )
                        if (LiquidScope.liquidModifier.chromaticAberration) chromaticAberration()
                    },
                    onDrawSurface = {
                        if ((tint ?: Color.Unspecified) != Color.Unspecified)
                            drawRect(tint ?: LiquidGLconfig.defaultTint)
                    }
                )
            ) {
                content()
            }
        }
    }

    // -------------------
    // UTILITIES
    // -------------------

    @Composable
    fun Float.dpToPx(): Float = with(LocalDensity.current) { this@dpToPx.toPx() }
}
