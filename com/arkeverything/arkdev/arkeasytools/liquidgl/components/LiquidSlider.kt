package com.arkeverything.arkdev.arkeasytools.liquidgl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.fastRoundToInt
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.catalog.utils.DampedDragAnimation
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow
import com.kyant.capsule.ContinuousCapsule
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.util.*

// Reusable visual config (tweakable)
data class LiquidSliderConfig(
    val blurDp: Dp? = 8.dp,
    val lensXDp: Dp? = 10.dp,
    val lensYDp: Dp? = 14.dp,
    val chromaticAberration: Boolean? = true,
    val tint: Color? = null,
    val surfaceColor: Color? = null,
    val shadowRadiusDp: Dp? = 4.dp,
    val innerShadowRadiusDp: Dp? = 4.dp,
    val reflectionOpacity: Float? = 0.18f,
    val highlightWidthDp: Dp? = 12.dp,
    val isInteractive: Boolean = true
)

/**
 * LiquidSlider — fixed and usable:
 *  - value() : Float supplier (host state)
 *  - onValueChange(Float) : called continuously during drag/tap
 *  - job: optional suspend callback invoked when user finishes interaction (drag end or tap)
 *
 * Notes:
 *  - Dp -> px conversions ONLY when calling blur()/lens() which expect Float pixels in kyant.
 *  - Shadow/InnerShadow are passed Dp (as kyant examples do).
 *  - DampedDragAnimation is created inside BoxWithConstraints so it knows track width for drag -> value math.
 */
@Composable
fun LiquidSlider(
    value: () -> Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    visibilityThreshold: Float = 0.001f,
    backdrop: Backdrop,
    config: LiquidSliderConfig = LiquidSliderConfig(),
    job: (suspend (Float) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isLightTheme = !isSystemInDarkTheme()
    val accentColor = config.tint ?: if (isLightTheme) Color(0xFF0088FF) else Color(0xFF0091FF)
    val trackColor = config.surfaceColor ?: if (isLightTheme) Color(0xFF787878).copy(alpha = 0.2f) else Color(0xFF787880).copy(alpha = 0.36f)

    val density = LocalDensity.current
    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    val trackBackdrop = rememberLayerBackdrop()
    val animationScope = rememberCoroutineScope()
    var didDrag by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        // track width in px (float)
        val trackWidthPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)

        // create DampedDragAnimation inside this scope so we can convert pixel deltas correctly
        val damped = remember(animationScope, trackWidthPx) {
            DampedDragAnimation(
                animationScope = animationScope,
                initialValue = value().coerceIn(valueRange),
                valueRange = valueRange,
                visibilityThreshold = visibilityThreshold,
                initialScale = 1f,
                pressedScale = 1.5f,
                onDragStarted = {},
                onDragStopped = {
                    if (didDrag) {
                        val final = targetValue.coerceIn(valueRange)
                        onValueChange(final)
                        job?.let { s -> animationScope.launch { s(final) } }
                        didDrag = false
                    }
                },
                onDrag = { _, dragAmount ->
                    if (!didDrag) didDrag = dragAmount.x != 0f
                    val delta = (valueRange.endInclusive - valueRange.start) * (dragAmount.x / trackWidthPx)
                    val candidate = (targetValue + if (isLtr) delta else -delta).coerceIn(valueRange)
                    // update host in real time and reflect in animation
                    onValueChange(candidate)
                    updateValue(candidate)
                }
            )
        }

        // sync host-driven value changes into the damped animation target
        LaunchedEffect(damped) {
            snapshotFlow { value() }
                .collectLatest { v ->
                    val clamped = v.coerceIn(valueRange)
                    if (damped.targetValue != clamped) {
                        damped.updateValue(clamped)
                    }
                }
        }

        // Track (background + filled)
        Box(Modifier.layerBackdrop(trackBackdrop)) {
            // inactive track
            Box(
                Modifier
                    .clip(ContinuousCapsule)
                    .background(trackColor)
                    .pointerInput(Unit) {
                        detectTapGestures { pos ->
                            val delta = (valueRange.endInclusive - valueRange.start) * (pos.x / trackWidthPx)
                            val target = (if (isLtr) valueRange.start + delta else valueRange.endInclusive - delta).coerceIn(valueRange)
                            damped.animateToValue(target)
                            onValueChange(target)
                            job?.let { s -> animationScope.launch { s(target) } }
                        }
                    }
                    .height(6.dp)
                    .fillMaxWidth()
            )

            // filled portion (progress)
            Box(
                Modifier
                    .clip(ContinuousCapsule)
                    .background(accentColor)
                    .height(6.dp)
                    .layout { measurable, constraintsInside ->
                        val placeable = measurable.measure(constraintsInside)
                        val progress = damped.progress.coerceIn(0f, 1f)
                        val widthPx = (constraintsInside.maxWidth * progress).fastRoundToInt().coerceAtLeast(0)
                        layout(widthPx, placeable.height) {
                            placeable.place(0, 0)
                        }
                    }
            )
        }

        // Thumb / handle (moving element)
        Box(
            Modifier
                .graphicsLayer {
                    val progress = damped.progress.coerceIn(0f, 1f)
                    val thumbCenterX = trackWidthPx * progress
                    val tx = (thumbCenterX - size.width / 2f).coerceIn(-size.width / 4f, trackWidthPx - size.width * 3f / 4f)
                    translationX = if (isLtr) tx else -tx
                }
                .then(damped.modifier)
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { ContinuousCapsule },
                    effects = {
                        val blurPx = with(density) { (config.blurDp ?: 8.dp).toPx() }
                        val lensXPx = with(density) { (config.lensXDp ?: 10.dp).toPx() }
                        val lensYPx = with(density) { (config.lensYDp ?: 14.dp).toPx() }
                        val press = damped.pressProgress
                        blur(blurPx * (1f - press))
                        lens(lensXPx * press, lensYPx * press, chromaticAberration = config.chromaticAberration ?: true)
                    },
                    highlight = {
                        // keep highlight simple and type-safe: use ambient but vary alpha with pressProgress
                        val progress = damped.pressProgress
                        Highlight.Ambient.copy(alpha = progress.coerceIn(0f, 1f))
                    },
                    shadow = { Shadow(radius = config.shadowRadiusDp ?: 4.dp) },
                    innerShadow = {
                        val pr = damped.pressProgress
                        InnerShadow(radius = (config.innerShadowRadiusDp ?: 4.dp) * pr, alpha = pr)
                    },
                    layerBlock = {
                        scaleX = damped.scaleX
                        scaleY = damped.scaleY
                        val velocity = damped.velocity / 10f
                        scaleX /= 1f - (velocity * 0.75f).fastCoerceIn(-0.2f, 0.2f)
                        scaleY *= 1f - (velocity * 0.25f).fastCoerceIn(-0.2f, 0.2f)
                    },
                    onDrawSurface = {
                        val progress = damped.pressProgress
                        drawRect(Color.White.copy(alpha = 1f - progress))
                    }
                )
                .size(40.dp, 24.dp)
        )
    } // BoxWithConstraints
}
