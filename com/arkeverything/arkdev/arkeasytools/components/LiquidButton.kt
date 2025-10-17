package com.arkeverything.arkdev.arkeasytools.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkeverything.arkdev.arkeasytools.Config.LiquidGLconfig
import com.arkeverything.arkdev.arkeasytools.LiquidScope
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.catalog.utils.InteractiveHighlight
import com.kyant.backdrop.drawBackdrop
import com.kyant.capsule.ContinuousCapsule
import androidx.compose.runtime.remember

@Composable
fun LiquidButton(
    onClick: () -> Unit,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = LiquidGLconfig.buttonIsInteractive,
    content: @Composable RowScope.() -> Unit
) {
    val lm = LiquidScope.liquidModifier
    val animationScope = rememberCoroutineScope()
    val interactiveHighlight = remember(animationScope) { InteractiveHighlight(animationScope) }

    Row(
        modifier
            .drawBackdrop(
                backdrop = backdrop,
                shape = { ContinuousCapsule },
                effects = {
                    // You can also hook these effects from LiquidGLconfig if needed
                },
                layerBlock = if (isInteractive) {
                    {
                        val progress = interactiveHighlight.pressProgress
                        val offset = interactiveHighlight.offset
                        translationX = offset.x
                        translationY = offset.y
                        scaleX = 1f + progress * 0.05f
                        scaleY = 1f + progress * 0.05f
                    }
                } else null,
                onDrawSurface = {
                    if (LiquidGLconfig.buttonTint != Color.Unspecified)
                        drawRect(LiquidGLconfig.buttonTint)
                }
            )
            .clickable(
                interactionSource = null,
                indication = if (isInteractive) null else LocalIndication.current,
                onClick = onClick
            )
            .then(if (isInteractive) interactiveHighlight.modifier else Modifier)
            .height(LiquidGLconfig.buttonHeight)
            .padding(horizontal = LiquidGLconfig.buttonHorizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}
