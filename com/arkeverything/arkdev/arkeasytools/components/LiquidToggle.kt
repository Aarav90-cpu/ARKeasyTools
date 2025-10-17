package com.arkeverything.arkdev.arkeasytools.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.arkeverything.arkdev.arkeasytools.Config.LiquidGLconfig
import com.arkeverything.arkdev.arkeasytools.assets.DampedDragAnimation
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import kotlinx.coroutines.launch

@Composable
fun LiquidToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    backdrop: Backdrop
) {
    val cfg = LiquidGLconfig
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Use a thumb size derived from toggleHeight if explicit thumb size isn't in config
    val thumbDp = (cfg.toggleHeight - 4.dp).coerceAtLeast(12.dp) // safe fallback

    // create an instance of the reusable DampedDragAnimation
    val dragAnimation = remember(scope) {
        DampedDragAnimation(
            animationScope = scope,
            initialValue = if (checked) 1f else 0f,
            valueRange = 0f..1f,
            onDrag = { /* optional live callback */ },
            onDragStopped = { final ->
                val newChecked = final > 0.5f
                onCheckedChange(newChecked)
            }
        )
    }

    // keep instance in sync when external checked changes
    LaunchedEffect(checked) {
        val target = if (checked) 1f else 0f
        dragAnimation.animateToValue(target)
    }

    BoxWithConstraints(modifier = modifier.size(cfg.toggleWidth, cfg.toggleHeight)) {
        val totalWidthPx = constraints.maxWidth.toFloat()
        val thumbPx = with(density) { thumbDp.toPx() }
        val travelPx = (totalWidthPx - thumbPx).coerceAtLeast(1f)

        // background / track (tap to toggle)
        Box(
            Modifier
                .pointerInput(Unit) {
                    detectTapGestures { pos: Offset ->
                        val tappedNorm = (pos.x / totalWidthPx).coerceIn(0f, 1f)
                        val target = tappedNorm
                        scope.launch {
                            dragAnimation.animateToValue(target)
                            // onDragStopped will call onCheckedChange
                        }
                    }
                }
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { ContinuousCapsule },
                    effects = {
                        vibrancy()
                        blur(with(density) { cfg.defaultBlurRadius.dp.toPx() })
                        lens(with(density) { cfg.defaultRefractionAmount.dp.toPx() }, with(density) { cfg.defaultRefractionHeight.dp.toPx() })
                    },
                    onDrawSurface = {
                        val bg = if (dragAnimation.targetValue > 0.5f) cfg.toggleAccentColor else cfg.toggleTrackColor
                        drawRect(bg)
                    }
                )
                .size(cfg.toggleWidth, cfg.toggleHeight)
        )

        // thumb (draggable)
        Box(
            Modifier
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { /* optional press visuals */ },
                        onDrag = { change: PointerInputChange, dragAmount: Offset ->
                            change.consume()
                            val deltaFraction = dragAmount.x / travelPx
                            dragAnimation.drag(deltaFraction)
                            // optionally live update: onCheckedChange(dragAnimation.targetValue > 0.5f)
                        },
                        onDragEnd = {
                            val snapped = if (dragAnimation.targetValue > 0.5f) 1f else 0f
                            scope.launch {
                                dragAnimation.animateToValue(snapped)
                                // onDragStopped will call onCheckedChange
                            }
                        }
                    )
                }
                .graphicsLayer {
                    // translation in px, safe inside composable scope
                    translationX = dragAnimation.progress * travelPx
                }
                .drawBackdrop(
                    backdrop = rememberCombinedBackdrop(backdrop, backdrop),
                    shape = { ContinuousCapsule },
                    effects = {
                        vibrancy()
                        blur(with(density) { cfg.defaultBlurRadius.dp.toPx() })
                        lens(with(density) { cfg.defaultRefractionAmount.dp.toPx() }, with(density) { cfg.defaultRefractionHeight.dp.toPx() })
                    },
                    onDrawSurface = { drawRect(Color.White) }
                )
                .size(thumbDp)
        )
    }
}
