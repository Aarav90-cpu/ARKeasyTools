package com.arkstudios.arkeasytools.fromkyant.BackCapsule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.shape.RoundedCornerShape
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule

object BackCapsule {

    class Builder {
        var cornerRadius: Dp = 16.dp
        var blurRadius: Dp = 12.dp
        var refractionHeight: Dp = 12.dp
        var refractionAmount: Dp = 24.dp
        var chromaticAberration: Boolean = false
        var bleedOpacity: Float = 0.15f

        enum class ShapeKind { Continuous, Rounded, Rectangle }
        var shapeKind: ShapeKind = ShapeKind.Continuous
        var roundedCornerRadius: Dp = cornerRadius

        var width: Dp = 320.dp
        var height: Dp = 180.dp

        internal var animationBlock: (suspend BoxScope.() -> Unit)? = null
        internal var jobBlock: (BoxScope.() -> Unit)? = null

        fun cornerRadius(value: Dp) { cornerRadius = value; roundedCornerRadius = value }
        fun blurRadius(value: Dp) { blurRadius = value }
        fun refractionHeight(value: Dp) { refractionHeight = value }
        fun refractionAmount(value: Dp) { refractionAmount = value }
        fun chromaticAberration(value: Boolean) { chromaticAberration = value }
        fun bleedOpacity(value: Float) { bleedOpacity = value }
        fun shape(value: ShapeKind) { shapeKind = value }
        fun size(w: Dp, h: Dp) { width = w; height = h }

        fun animation(block: suspend BoxScope.() -> Unit) { animationBlock = block }
        fun job(block: BoxScope.() -> Unit) { jobBlock = block }

        internal fun provideShape(): Shape {
            return when (shapeKind) {
                ShapeKind.Continuous -> ContinuousCapsule
                ShapeKind.Rounded -> RoundedCornerShape(roundedCornerRadius)
                ShapeKind.Rectangle -> RoundedCornerShape(0.dp)
            }
        }
    }

    /**
     * Composable invoke so you can call:
     *   BackCapsule { cornerRadius(24.dp); size(300.dp,160.dp) } content@{ /* children */ }
     */
    @Composable
    operator fun invoke(
        modifier: Modifier = Modifier,
        configure: Builder.() -> Unit = {},
        content: @Composable BoxScope.() -> Unit = {}
    ) {
        val builder = remember { Builder().apply(configure) }
        val backdrop = rememberLayerBackdrop()
        val density = LocalDensity.current

        Box(
            modifier = modifier
                .size(builder.width, builder.height)
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { builder.provideShape() },
                    effects = {
                        val blurPx = with(density) { builder.blurRadius.toPx() }
                        val refH = with(density) { builder.refractionHeight.toPx() }
                        val refA = with(density) { builder.refractionAmount.toPx() }

                        blur(blurPx)
                        lens(refH, refA, chromaticAberration = builder.chromaticAberration)
                        vibrancy()
                    },
                    onDrawSurface = {
                        drawRect(Color.White.copy(alpha = builder.bleedOpacity))
                    }
                )
        ) {
            builder.jobBlock?.invoke(this)
            builder.animationBlock?.let { anim ->
                LaunchedEffect(anim) { anim.invoke(this@Box) }
            }
            content()
        }
    }
}
