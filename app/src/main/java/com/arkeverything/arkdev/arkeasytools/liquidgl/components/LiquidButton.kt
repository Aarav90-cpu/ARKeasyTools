package com.arkeverything.arkdev.arkeasytools.liquidgl.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceAtMost
import androidx.compose.ui.util.lerp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.catalog.utils.InteractiveHighlight
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.Indication

// Config class for fully customizing the button
data class LiquidButtonConfig(
    val isInteractive: Boolean = true,
    val tint: Color = Color.Unspecified,
    val surfaceColor: Color = Color.Unspecified,
    val blur: Dp = 2.dp,
    val lensX: Dp = 12.dp,
    val lensY: Dp = 24.dp,
    val height: Dp = 48.dp,
    val horizontalPadding: Dp = 16.dp
)

@Composable
fun LiquidButton(
    onClick: () -> Unit = {},
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    config: LiquidButtonConfig = LiquidButtonConfig(),
    job: (suspend () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    val animationScope = rememberCoroutineScope()
    val interactiveHighlight = remember(animationScope) {
        InteractiveHighlight(animationScope)
    }

    var finalModifier = modifier
        .drawBackdrop(
            backdrop = backdrop,
            shape = { ContinuousCapsule },
            effects = {
                vibrancy()
                blur(config.blur.toPx())
                lens(config.lensX.toPx(), config.lensY.toPx())
            },
            layerBlock = if (config.isInteractive) {
                {
                    val width = size.width
                    val height = size.height
                    val progress = interactiveHighlight.pressProgress
                    val scale = lerp(1f, 1f + 4.dp.toPx() / size.height, progress)
                    val maxOffset = size.minDimension
                    val initialDerivative = 0.05f
                    val offset = interactiveHighlight.offset
                    translationX = maxOffset * tanh(initialDerivative * offset.x / maxOffset)
                    translationY = maxOffset * tanh(initialDerivative * offset.y / maxOffset)
                    val maxDragScale = 4.dp.toPx() / size.height
                    val offsetAngle = atan2(offset.y, offset.x)
                    scaleX = scale + maxDragScale * abs(cos(offsetAngle) * offset.x / size.maxDimension) *
                            (width / height).fastCoerceAtMost(1f)
                    scaleY = scale + maxDragScale * abs(sin(offsetAngle) * offset.y / size.maxDimension) *
                            (height / width).fastCoerceAtMost(1f)
                }
            } else null,
            onDrawSurface = {
                if (config.tint.isSpecified) {
                    drawRect(config.tint, blendMode = BlendMode.Hue)
                    drawRect(config.tint.copy(alpha = 0.75f))
                }
                if (config.surfaceColor.isSpecified) {
                    drawRect(config.surfaceColor)
                }
            }
        )
        .clickable(
            interactionSource = remember { MutableInteractionSource() }, // <- this is required
            indication = if (config.isInteractive) null else LocalIndication.current,
            role = Role.Button,
            onClick = {
                onClick()
                job?.let { animationScope.launch { it() } }
            }
        )
        .height(config.height)
        .padding(horizontal = config.horizontalPadding)

    if (config.isInteractive) {
        finalModifier = finalModifier
            .then(interactiveHighlight.modifier)
            .then(interactiveHighlight.gestureModifier)
    }

    Row(
        modifier = finalModifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}
