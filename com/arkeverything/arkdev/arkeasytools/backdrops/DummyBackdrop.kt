package com.arkeverything.arkdev.arkeasytools.backdrops

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawWithContent
import com.arkeverything.arkdev.arkeasytools.Config.LiquidGLconfig

@Composable
fun DummyBackdrop(
    modifier: Modifier = Modifier,
    color: Color? = null,
    blurRadius: Float? = null,
    tint: Color? = null,
    layers: Int = 1 // number of layered dummy backdrops
) {
    repeat(layers) { layerIndex ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = 0.8f / (layerIndex + 1) // progressively lighter layers
                }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        tint ?: LiquidGLconfig.defaultTint,
                        blendMode = BlendMode.SrcOver
                    )
                }
                .background(color ?: Color.LightGray.copy(alpha = 0.3f / (layerIndex + 1)))
        )
    }
}

@Composable
fun rememberDummyBackdrop(
    modifier: Modifier = Modifier,
    color: Color? = null,
    blurRadius: Float? = null,
    tint: Color? = null,
    layers: Int = 1
): @Composable () -> Unit = remember(modifier, color, blurRadius, tint, layers) {
    { DummyBackdrop(modifier, color, blurRadius, tint, layers) }
}
