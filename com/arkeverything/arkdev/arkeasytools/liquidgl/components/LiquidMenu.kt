// Menu.kt
package com.arkeverything.arkdev.arkeasytools.liquidgl.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable


data class MenuOption(
    val label: String,
    val job: (suspend () -> Unit)? = null,
    val color: Color = Color.White
)

data class MenuConfig(
    val circleSize: Dp = 60.dp,
    val optionHeight: Dp = 50.dp,
    val optionSpacing: Dp = 8.dp,
    val baseWidth: Dp = 120.dp,
    val extraWidthPerOption: Dp = 8.dp,
    val blur: Dp = 8.dp,
    val lensX: Dp = 12.dp,
    val lensY: Dp = 12.dp,
    val chromaticAberration: Boolean = true,
    val backgroundTint: Color = Color.White.copy(alpha = 0.06f)
)

data class LiquidMenuOption(
    val label: String,
    val job: (() -> Unit)? = null
)

data class LiquidMenuConfig(
    val circleSize: androidx.compose.ui.unit.Dp,
    val optionHeight: androidx.compose.ui.unit.Dp,
    val optionSpacing: androidx.compose.ui.unit.Dp,
    val baseWidth: androidx.compose.ui.unit.Dp,
    val extraWidthPerOption: androidx.compose.ui.unit.Dp,
    val blurDp: androidx.compose.ui.unit.Dp,
    val lensXDp: androidx.compose.ui.unit.Dp,
    val lensYDp: androidx.compose.ui.unit.Dp,
    val backgroundTint: androidx.compose.ui.graphics.Color
)

@Composable
fun LiquidMenu(
    options: List<LiquidMenuOption>,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    config: LiquidMenuConfig,
    initiallyExpanded: Boolean = false,
    onOpenChanged: ((Boolean) -> Unit)? = null
) {
    // Placeholder UI, make your full expanding menu later
}

@Composable
fun LiquidExpandableMenu(
    options: List<MenuOption>,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    config: MenuConfig = MenuConfig(),
    initiallyExpanded: Boolean = false,
    onOpenChanged: ((Boolean) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    val optionCount = options.size.coerceAtLeast(1)
    val expandedHeight = config.circleSize + (config.optionSpacing * (optionCount - 1)) +
            (config.optionHeight * optionCount)
    val expandedWidth = config.baseWidth + config.extraWidthPerOption * (optionCount - 1)
    val collapsedSize = config.circleSize

    val animWidth by animateDpAsState(if (expanded) expandedWidth else collapsedSize)
    val animHeight by animateDpAsState(if (expanded) expandedHeight else collapsedSize)
    val animCorner by animateDpAsState(if (expanded) 12.dp else (collapsedSize / 2))
    val pressScale by animateFloatAsState(if (expanded) 1.02f else 1f)

    Box(
        modifier = modifier
            .size(animWidth, animHeight)
            .clip(RoundedCornerShape(animCorner))
            .drawBackdrop(
                backdrop = backdrop,
                shape = { ContinuousCapsule },
                effects = {
                    vibrancy()
                    blur(config.blur.toPx())
                    lens(config.lensX.toPx(), config.lensY.toPx(), chromaticAberration = config.chromaticAberration)
                },
                onDrawSurface = { drawRect(config.backgroundTint) }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                expanded = !expanded
                onOpenChanged?.invoke(expanded)
            }
            .padding(6.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        if (!expanded) {
            val first = options.firstOrNull()
            Box(
                Modifier
                    .size(config.circleSize)
                    .clip(RoundedCornerShape(config.circleSize / 2))
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(first?.label ?: "+", color = first?.color ?: Color.White)
            }
            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(config.optionSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            options.forEachIndexed { idx, option ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(config.optionHeight)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Transparent)
                        .clickable {
                            option.job?.let { jb -> scope.launch { jb() } }
                            expanded = false
                            onOpenChanged?.invoke(false)
                        }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(option.label, color = option.color)
                }
            }
        }
    }
}
