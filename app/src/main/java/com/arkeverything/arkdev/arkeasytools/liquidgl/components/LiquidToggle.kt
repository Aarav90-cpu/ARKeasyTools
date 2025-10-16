package com.arkeverything.arkdev.arkeasytools.liquidgl.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceIn
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
import androidx.compose.ui.util.lerp

/**
 * Config for LiquidToggle — all fields optional, fallback to sensible defaults.
 * Add whatever you want here (sizes, colors, blur amounts, etc.)
 */
data class ToggleConfig(
    val trackWidthDp: Float? = null,             // not used for layout here — kept for parity
    val dragWidthDp: Float? = 20f,               // used to convert to px: amount of thumb travel
    val blurDp: Int? = 8,                        // blur (Dp) as Int to simplify calls below
    val lensXDp: Int? = 5,
    val lensYDp: Int? = 10,
    val chromaticAberration: Boolean? = true,
    val shadowRadiusDp: Int? = 4,
    val innerShadowRadiusDp: Int? = 4,
    val onTint: Color? = null,
    val offTint: Color? = null,
    val trackOffColor: Color? = null,
    val pressedScale: Float? = 1.5f
)

/**
 * LiquidToggle — keeps Kyant's structure/behavior but:
 *  - accepts a ToggleConfig for customization
 *  - accepts an optional suspend job: job(Boolean) which is executed when the toggle final value changes
 *  - if job == null, toggle acts in test/local mode (just flips)
 *
 * Params:
 *  - selected: () -> Boolean   // host state supplier
 *  - onSelect: (Boolean) -> Unit  // host commit callback
 *  - backdrop: Backdrop  // kyant backdrop
 *  - config: ToggleConfig
 *  - job: suspend (Boolean) -> Unit ?  // optional job executed on toggle completion
 *  - modifier: Modifier = Modifier
 */
@Composable
fun LiquidToggle(
    selected: () -> Boolean,
    onSelect: (Boolean) -> Unit,
    backdrop: Backdrop,
    config: ToggleConfig = ToggleConfig(),
    job: (suspend (Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // theme & colors (defaults mirror your Kyant snippet)
    val isLightTheme = !isSystemInDarkTheme()
    val accentColor = config.onTint ?: if (isLightTheme) Color(0xFF34C759) else Color(0xFF30D158)
    val defaultTrackColor = config.trackOffColor ?: if (isLightTheme) Color(0xFF787878).copy(alpha = 0.2f) else Color(0xFF787880).copy(alpha = 0.36f)

    val density = LocalDensity.current
    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr

    // convert configured dp-ish numbers to pixels where needed
    val dragWidthPx = with(density) { (config.dragWidthDp ?: 20f).dp.toPx() } // thumb travel in px
    val blurPxBase = with(density) { (config.blurDp ?: 8).dp.toPx() }
    val lensXBase = with(density) { (config.lensXDp ?: 5).dp.toPx() }
    val lensYBase = with(density) { (config.lensYDp ?: 10).dp.toPx() }
    val pressedScale = config.pressedScale ?: 1.5f

    val animationScope = rememberCoroutineScope()
    var didDrag by remember { mutableStateOf(false) }

    // fraction is 0..1 representing toggle position (0=off,1=on)
    var fraction by remember { mutableFloatStateOf(if (selected()) 1f else 0f) }

    val dampedDragAnimation = remember(animationScope) {
        DampedDragAnimation(
            animationScope = animationScope,
            initialValue = fraction,
            valueRange = 0f..1f,
            visibilityThreshold = 0.001f,
            initialScale = 1f,
            pressedScale = pressedScale,
            onDragStarted = {},
            onDragStopped = {
                // inside DampedDragAnimation receiver: targetValue exists here
                if (didDrag) {
                    fraction = if (targetValue >= 0.5f) 1f else 0f
                    onSelect(fraction == 1f)
                    // run optional suspend job
                    job?.let { jb ->
                        animationScope.launch { jb(fraction == 1f) }
                    }
                    didDrag = false
                } else {
                    // short tap: toggle
                    fraction = if (selected()) 0f else 1f
                    onSelect(fraction == 1f)
                    job?.let { jb -> animationScope.launch { jb(fraction == 1f) } }
                }
            },
            onDrag = { _, dragAmount ->
                if (!didDrag) didDrag = dragAmount.x != 0f
                // convert pixel drag to 0..1 fraction using dragWidthPx as scale
                val delta = dragAmount.x / (dragWidthPx.takeIf { it > 0f } ?: 1f)
                fraction = if (isLtr) (fraction + delta).fastCoerceIn(0f, 1f) else (fraction - delta).fastCoerceIn(0f, 1f)
            }
        )
    }

    // keep DampedDragAnimation in sync with our fraction when external state changes
    LaunchedEffect(dampedDragAnimation) {
        snapshotFlow { fraction }
            .collectLatest { f ->
                // ensure damped knows the new fraction
                dampedDragAnimation.updateValue(f)
            }
    }

    // if host state changed externally, animate to it
    LaunchedEffect(selected) {
        snapshotFlow { selected() }
            .collectLatest { isSelected ->
                val target = if (isSelected) 1f else 0f
                if (target != fraction) {
                    fraction = target
                    dampedDragAnimation.animateToValue(target)
                }
            }
    }

    val trackBackdrop = rememberLayerBackdrop()

    Box(modifier = modifier, contentAlignment = Alignment.CenterStart) {
        // track (background) — same visual as Kyant; uses drawBehind to blend track colors by fraction
        Box(
            Modifier
                .layerBackdrop(trackBackdrop)
                .clip(ContinuousCapsule)
                .drawBehind {
                    val frac = dampedDragAnimation.value
                    val off = defaultTrackColor
                    val onC = accentColor
                    drawRect(lerp(off, onC, frac))
                }
                .size(64.dp, 28.dp)
        )

        // knob / moving element — keep kyant's drawBackdrop hierarchy and onDrawSurface
        Box(
            Modifier
                .graphicsLayer {
                    val frac = dampedDragAnimation.value
                    val paddingPx = with(density) { 2.dp.toPx() }
                    translationX = if (isLtr) lerp(paddingPx, paddingPx + dragWidthPx, frac) else lerp(-paddingPx, -(paddingPx + dragWidthPx), frac)
                }
                .semantics { role = Role.Switch }
                .then(dampedDragAnimation.modifier)
                .drawBackdrop(
                    backdrop = rememberCombinedBackdrop(
                        backdrop,
                        rememberBackdrop(trackBackdrop) { drawBackdrop ->
                            val progress = dampedDragAnimation.pressProgress
                            val scaleX = lerp(2f / 3f, 0.75f, progress)
                            val scaleY = lerp(0f, 0.75f, progress)
                            scale(scaleX, scaleY) { drawBackdrop() }
                        }
                    ),
                    shape = { ContinuousCapsule },
                    effects = {
                        val progress = dampedDragAnimation.pressProgress
                        // blur and lens expect pixel floats — use bases computed above multiplied by progress
                        blur(blurPxBase * (1f - progress))
                        lens(lensXBase * progress, lensYBase * progress, chromaticAberration = config.chromaticAberration ?: true)
                    },
                    highlight = {
                        val progress = dampedDragAnimation.pressProgress
                        // keep kyant's Highlight usage but with safe values — we use Ambient and modulate alpha
                        Highlight.Ambient.copy(
                            width = (Highlight.Ambient.width / 1.5f),
                            blurRadius = (Highlight.Ambient.blurRadius / 1.5f),
                            alpha = progress
                        )
                    },
                    shadow = {
                        Shadow(radius = (config.shadowRadiusDp ?: 4).dp, color = Color.Black.copy(alpha = 0.05f))
                    },
                    innerShadow = {
                        val progress = dampedDragAnimation.pressProgress
                        InnerShadow(radius = (config.innerShadowRadiusDp ?: 4).dp * progress, alpha = progress)
                    },
                    layerBlock = {
                        scaleX = dampedDragAnimation.scaleX
                        scaleY = dampedDragAnimation.scaleY
                        val velocity = dampedDragAnimation.velocity / 50f
                        scaleX /= 1f - (velocity * 0.75f).fastCoerceIn(-0.2f, 0.2f)
                        scaleY *= 1f - (velocity * 0.25f).fastCoerceIn(-0.2f, 0.2f)
                    },
                    onDrawSurface = {
                        val progress = dampedDragAnimation.pressProgress
                        // preserved exactly as you told me to:
                        drawRect(Color.White.copy(alpha = 1f - progress))
                    }
                )
                .size(40.dp, 24.dp)
        )
    }
}
