package com.arkeverything.arkdev.arkeasytools.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.arkeverything.arkdev.arkeasytools.Config.LiquidGLconfig
import com.arkeverything.arkdev.arkeasytools.assets.DampedDragAnimation
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun LiquidBottomTabs(
    selectedTabIndex: () -> Int,
    onTabSelected: (index: Int) -> Unit,
    tabsCount: Int,
    modifier: Modifier = Modifier,
    backdrop: Backdrop,
    tint: Color? = null,
    content: @Composable RowScope.() -> Unit
) {
    val cfg = LiquidGLconfig
    val isLight = !isSystemInDarkTheme()
    val accent = tint ?: if (isLight) cfg.bottomTabAccentColor else cfg.bottomTabAccentColor
    val containerColor = if (isLight) cfg.bottomTabContainerColor else cfg.bottomTabContainerColor

    val tabsBackdrop = rememberLayerBackdrop()
    val combinedBackdrop = rememberCombinedBackdrop(backdrop, tabsBackdrop)
    val layoutDir = LocalLayoutDirection.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // Precompute px values in composable scope
    val blurPx = with(density) { cfg.defaultBlurRadius.dp.toPx() }
    val lensAPx = with(density) { cfg.defaultRefractionAmount.dp.toPx() }
    val lensHPx = with(density) { cfg.defaultRefractionHeight.dp.toPx() }

    // Indicator animation state (instance of reusable DampedDragAnimation)
    val dragAnimation = remember(scope) {
        DampedDragAnimation(
            animationScope = scope,
            initialValue = selectedTabIndex().toFloat(),
            valueRange = 0f..(tabsCount - 1).toFloat(),
            onDrag = { /* optionally notify live */ },
            onDragStopped = { final ->
                val idx = final.roundToInt().coerceIn(0, tabsCount - 1)
                onTabSelected(idx)
            }
        )
    }

    // Keep in sync if external selection changes
    LaunchedEffect(selectedTabIndex) {
        snapshotFlow { selectedTabIndex() }.collect { idx ->
            // if external wants to change selection, animate indicator
            if (dragAnimation.targetValue != idx.toFloat()) {
                scope.launch {
                    dragAnimation.animateToValue(idx.toFloat())
                }
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(cfg.bottomTabContainerHeight),
        contentAlignment = Alignment.CenterStart
    ) {
        val fullWidthPx = constraints.maxWidth.toFloat()
        val tabWidth = fullWidthPx / tabsCount

        // Main visible row (user content)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(cfg.bottomTabPadding)
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { ContinuousCapsule },
                    effects = {
                        vibrancy()
                        blur(blurPx)
                        lens(lensAPx, lensHPx)
                    },
                    onDrawSurface = { drawRect(containerColor) }
                ),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )

        // Invisible layer for highlight/backdrop syncing (mirrors content for effects)
        Row(
            Modifier
                .alpha(0f)
                .layerBackdrop(tabsBackdrop)
                .fillMaxWidth()
                .padding(horizontal = cfg.bottomTabPadding),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )

        // Indicator/Thumb overlay: draggable + tappable
        Box(
            Modifier
                // gestures: drag & tap
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { /* optional: start press visuals */ },
                        onDrag = { change: PointerInputChange, dragAmount: Offset ->
                            change.consume()
                            val deltaFraction = dragAmount.x / tabWidth
                            dragAnimation.drag(deltaFraction)
                        },
                        onDragEnd = {
                            // snap to nearest index
                            val snapped = dragAnimation.targetValue.roundToInt().coerceIn(0, tabsCount - 1).toFloat()
                            scope.launch { dragAnimation.animateToValue(snapped) }
                        },
                        onDragCancel = {
                            val snapped = dragAnimation.targetValue.roundToInt().coerceIn(0, tabsCount - 1).toFloat()
                            scope.launch { dragAnimation.animateToValue(snapped) }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures { pos: Offset ->
                        val tapped = (pos.x / tabWidth).toInt().coerceIn(0, tabsCount - 1)
                        scope.launch { dragAnimation.animateToValue(tapped.toFloat()) }
                    }
                }
                .graphicsLayer {
                    // compute translationX in pixels based on normalized progress
                    translationX = if (layoutDir == LayoutDirection.Ltr) {
                        dragAnimation.progress * tabWidth
                    } else {
                        // RTL: position from right edge
                        size.width - (dragAnimation.progress + 1f) * tabWidth
                    }
                }
                .drawBackdrop(
                    backdrop = combinedBackdrop,
                    shape = { ContinuousCapsule },
                    effects = {
                        val p = dragAnimation.progress
                        // make lens/reactive to progress if you like; here small lens
                        lens(10.dp.toPx() * 1f, 12.dp.toPx() * 1f, chromaticAberration = true)
                    },
                    onDrawSurface = {
                        drawRect(accent.copy(alpha = 0.25f))
                    }
                )
                .height(cfg.bottomTabHeight)
                .fillMaxWidth(1f / tabsCount)
        )
    }
}
