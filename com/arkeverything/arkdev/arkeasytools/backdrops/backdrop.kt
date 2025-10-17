package com.arkeverything.arkdev.arkeasytools.backdrops

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import com.arkeverything.arkdev.arkeasytools.Config.LiquidGLconfig
import com.arkeverything.arkdev.arkeasytools.LiquidScope
import com.arkeverything.arkdev.arkeasytools.assets.chromaticAberration
import com.kyant.backdrop.Backdrop

/**
 * Full composable Backdrop using the layered backdrop from LayeredBackdrop.kt
 */
@Composable
fun Backdrop(
    modifier: Modifier = Modifier,
    blurRadius: Float = LiquidGLconfig.defaultBlurRadius,
    refractionAmount: Float = LiquidGLconfig.defaultRefractionAmount,
    refractionHeight: Float = LiquidGLconfig.defaultRefractionHeight,
    tint: Color = LiquidGLconfig.defaultTint,
    content: @Composable () -> Unit
) {
    // Compose-safe LayeredBackdrop from your LayeredBackdrop.kt
    val layeredBackdrop = rememberLayeredBackdrop()

    BoxWithConstraints(modifier = modifier) {
        // use width/height just to satisfy the compiler
        val width = maxWidth
        val height = maxHeight

        // Convert your LayeredBackdrop into a real Backdrop
        val backdrop: Backdrop = layeredBackdrop.toBackdrop()

        Box(
            Modifier.drawBackdrop(
                backdrop = backdrop,
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
