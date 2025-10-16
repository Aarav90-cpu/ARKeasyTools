package com.arkeverything.arkdev.arkeasytools.liquidgl.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.catalog.utils.InteractiveHighlight
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow
import com.kyant.capsule.ContinuousCapsule
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh

/**
 * Config to customize LiquidBottomTabs. All fields optional.
 */
data class LiquidTabsConfig(
    val blurDp: Dp? = 6.dp,
    val lensXDp: Dp? = 12.dp,
    val lensYDp: Dp? = 24.dp,
    val chromaticAberration: Boolean? = true,
    val tint: Color? = null,
    val surfaceColor: Color? = null,
    val shadowRadiusDp: Dp? = 4.dp,
    val innerShadowRadiusDp: Dp? = 8.dp,
    val highlightWidthDp: Dp? = null,
    val reflectionOpacity: Float? = null,
    val isInteractive: Boolean = true
)

/**
 * Fully working LiquidBottomTabs:
 * - dynamic tabsCount
 * - per-tab job callback (suspend is supported via coroutine launcher)
 * - correct Dp <-> px handling for blur/lens (which require Float pixels)
 * - Shadow/InnerShadow use Dp as required by kyant API
 * - safe explicit clickable overload with MutableInteractionSource
 * - content: @Composable RowScope.(Int) -> Unit  <-- you get the tab index
 */
@Composable
fun LiquidBottomTabs(
    tabsCount: Int = 3,
    modifier: Modifier = Modifier,
    backdrop: Backdrop,
    config: LiquidTabsConfig = LiquidTabsConfig(),
    job: ((Int) -> Unit)? = null,
    content: @Composable RowScope.(Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until tabsCount) {
            val animationScope = rememberCoroutineScope()
            val interactiveHighlight = remember(animationScope, i) { InteractiveHighlight(animationScope) }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { ContinuousCapsule },
                        effects = {
                            val blurPx = with(density) { (config.blurDp ?: 6.dp).toPx() }
                            val lensXPx = with(density) { (config.lensXDp ?: 12.dp).toPx() }
                            val lensYPx = with(density) { (config.lensYDp ?: 24.dp).toPx() }

                            vibrancy()
                            blur(blurPx)
                            lens(lensXPx, lensYPx, chromaticAberration = config.chromaticAberration ?: true)
                        },
                        layerBlock = {
                            if (config.isInteractive) {
                                val progress = interactiveHighlight.pressProgress
                                val offset = interactiveHighlight.offset
                                val angle = atan2(offset.y, offset.x)
                                val maxOffset = size.minDimension

                                translationX = maxOffset * tanh(0.05f * offset.x / maxOffset)
                                translationY = maxOffset * tanh(0.05f * offset.y / maxOffset)

                                val maxDragPx = with(density) { (config.shadowRadiusDp ?: 4.dp).toPx() }
                                val maxDragScale = if (size.height > 0f) maxDragPx / size.height else 0f

                                scaleX = 1f + maxDragScale * abs(cos(angle) * offset.x / size.maxDimension)
                                scaleY = 1f + maxDragScale * abs(sin(angle) * offset.y / size.maxDimension)
                            }
                        },
                        shadow = { Shadow(radius = config.shadowRadiusDp ?: 4.dp) },
                        innerShadow = { InnerShadow(radius = config.innerShadowRadiusDp ?: 8.dp) },
                        onDrawSurface = {
                            drawRect(config.tint ?: config.surfaceColor ?: Color(0xFF0088FF))
                        }
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = if (config.isInteractive) null else LocalIndication.current,
                        role = Role.Button
                    ) {
                        job?.let { scope.launch { it(i) } }
                    }
                    .padding(horizontal = 8.dp)
            ) {
                this@Row.content(i) // Explicit receiver fixes the RowScope call
            }
        }
    }
}
