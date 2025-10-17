package com.arkeverything.arkdev.arkeasytools.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import android.R.attr.translationX
import com.arkeverything.arkdev.arkeasytools.Config.LiquidGLconfig
import androidx.compose.runtime.snapshotFlow
import com.kyant.backdrop.Backdrop
import androidx.compose.ui.graphics.graphicsLayer
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import kotlinx.coroutines.flow.collectLatest
import com.arkeverything.arkdev.arkeasytools.assets.DampedDragAnimation
import com.arkeverything.arkdev.arkeasytools.assets.toPx
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import android.R.attr.thumbOffset


@Composable
fun LiquidSlider(
    value: () -> Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    val animationScope = rememberCoroutineScope()
    val dragAnimation = remember(animationScope) {
        DampedDragAnimation(
            animationScope = animationScope,
            initialValue = value(),
            valueRange = valueRange,
            onDrag = { updated -> onValueChange(updated) },
            onDragStopped = { final -> onValueChange(final) }
        )
    }

    LaunchedEffect(value) {
        snapshotFlow { value() }.collectLatest { newValue ->
            if (dragAnimation.targetValue != newValue) {
                dragAnimation.updateValue(newValue)
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        val sliderWidth = constraints.maxWidth.toFloat()
        val density = LocalDensity.current

// Convert sliderThumbWidth (Dp) to px first
        val thumbWidthPx: Float = with(density) { LiquidGLconfig.sliderThumbWidth.toPx() }

// Now compute offset in pixels
        val thumbOffsetPx = -thumbWidthPx / 2f + sliderWidth * dragAnimation.progress

// Convert back to Dp for Modifier.offset
        val thumbOffsetDp: Dp = with(density) { thumbOffsetPx.toDp() }

        // Compute blur / lens in pixels
        val blurPx = with(density) { LiquidGLconfig.sliderBlur.toPx() }
        val lensAmountPx = with(density) { LiquidGLconfig.sliderRefractionAmount.toPx() }
        val lensHeightPx = with(density) { LiquidGLconfig.sliderRefractionHeight.toPx() }

        // Track
        Box(
            Modifier
                .offset(x = thumbOffsetDp) // ✅ now it’s Dp
                .size(LiquidGLconfig.sliderThumbWidth, LiquidGLconfig.sliderThumbHeight)
                .fillMaxWidth()
                .height(LiquidGLconfig.sliderHeight)
                .background(LiquidGLconfig.sliderTrackColor)
                .pointerInput(animationScope) {
                    detectTapGestures { pos ->
                        val delta = (valueRange.endInclusive - valueRange.start) * (pos.x / sliderWidth)
                        val targetValue = (valueRange.start + delta).coerceIn(valueRange)
                        dragAnimation.animateToValue(targetValue)
                        onValueChange(targetValue)
                    }
                }
        )

        // Thumb
        Box(
            Modifier
                .offset(x = thumbOffsetDp)
                .size(
                    LiquidGLconfig.sliderThumbWidth,
                    LiquidGLconfig.sliderThumbHeight
                )
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { ContinuousCapsule },
                    effects = {
                        vibrancy()
                        blur(blurPx)
                        lens(lensAmountPx, lensHeightPx)
                    }
                )
        )
    }

}
