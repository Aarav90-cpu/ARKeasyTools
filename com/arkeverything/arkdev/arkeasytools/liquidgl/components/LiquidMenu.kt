// LiquidMenu.kt
package com.arkeverything.arkdev.arkeasytools.liquidgl.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousCapsule
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * One menu option. The composable `content` is optional — if provided it will be shown instead of label text.
 * The `job` is a suspend lambda run when the option is clicked.
 */
data class LiquidMenuOption(
    val id: Int,
    val label: String,
    val job: (suspend () -> Unit)? = null,
    val content: (@Composable (() -> Unit))? = null,
    val tint: Color? = null // per-option tint override
)

/**
 * Visual configuration for LiquidMenu. All fields optional with sensible defaults.
 */
data class LiquidMenuConfig(
    val circleSize: Dp = 56.dp,
    val optionHeight: Dp = 44.dp,
    val optionSpacing: Dp = 6.dp,
    val baseWidth: Dp = 140.dp,          // width base for 1 option; more options slightly widen
    val extraWidthPerOption: Dp = 8.dp,  // extra width added per option
    val maxOptions: Int = 10,
    val blurDp: Dp = 8.dp,
    val lensXDp: Dp = 12.dp,
    val lensYDp: Dp = 12.dp,
    val chromaticAberration: Boolean = true,
    val backgroundTint: Color? = Color(0xFFFFFFFF).copy(alpha = 0.06f),
    val optionTextStyle: TextStyle = TextStyle(fontSize = 14.sp),
    val cornerExpanded: Dp = 12.dp,
    val openScale: Float = 1.02f
)

/**
 * LiquidMenu composable.
 *
 * - options: list of LiquidMenuOption (up to config.maxOptions; extra options are ignored)
 * - backdrop: kyant Backdrop to draw glass effects
 * - config: visual config
 * - initiallyExpanded: whether the menu starts expanded
 *
 * Behavior:
 * - Small circle when collapsed
 * - Smoothly animates to rectangle when expanded
 * - Menu height grows with the number of visible options
 * - Each option runs its suspend job when clicked (launched in a coroutine)
 */
@Composable
fun LiquidMenu(
    options: List<LiquidMenuOption>,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    config: LiquidMenuConfig = LiquidMenuConfig(),
    initiallyExpanded: Boolean = false,
    onOpenChanged: ((Boolean) -> Unit)? = null
) {
    // clamp options to allowed max
    val visibleOptions = remember(options, config.maxOptions) {
        options.take(config.maxOptions.coerceAtLeast(1))
    }

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    // measure expanded sizes
    val optionCount = visibleOptions.size.coerceAtLeast(1)
    val expandedHeight = config.circleSize + (config.optionSpacing * (optionCount - 1)) +
            (config.optionHeight * optionCount)
    val expandedWidth = config.baseWidth + config.extraWidthPerOption * (optionCount - 1)
    val collapsedSize = config.circleSize

    // animated dimensions
    val animWidth by animateDpAsState(if (expanded) expandedWidth else collapsedSize)
    val animHeight by animateDpAsState(if (expanded) expandedHeight else collapsedSize)
    val animCornerRadius by animateDpAsState(if (expanded) config.cornerExpanded else (collapsedSize / 2))
    val pressScale by animateFloatAsState(if (expanded) config.openScale else 1f)

    // pixel floats for kyant blur/lens
    val blurPx = with(density) { config.blurDp.toPx() }
    val lensXPx = with(density) { config.lensXDp.toPx() }
    val lensYPx = with(density) { config.lensYDp.toPx() }

    // backdrop layer for inner effects
    val layerBackdrop = rememberLayerBackdrop()

    // clickable interactionSource
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(animWidth, animHeight)
            .clip(RoundedCornerShape(animCornerRadius))
            .drawBackdrop(
                backdrop = backdrop,
                shape = { ContinuousCapsule },
                effects = {
                    vibrancy()
                    // kyant expects pixel floats for blur/lens
                    blur(blurPx)
                    lens(lensXPx, lensYPx, chromaticAberration = config.chromaticAberration)
                },
                onDrawSurface = {
                    // background tint with fallback
                    drawRect(config.backgroundTint ?: Color(0x00000000))
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = if (expanded) null else LocalIndication.current,
                role = Role.Button
            ) {
                // top-level click toggles expand/collapse
                expanded = !expanded
                onOpenChanged?.invoke(expanded)
            }
            .padding(6.dp)
            .then(if (pressScale != 1f) Modifier else Modifier),
        contentAlignment = Alignment.TopCenter
    ) {
        // if collapsed -> just show a centered little dot (or first option content)
        if (!expanded) {
            val first = visibleOptions.firstOrNull()
            Box(
                Modifier
                    .size(config.circleSize)
                    .clip(RoundedCornerShape(config.circleSize / 2))
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                // show either custom content or label text of first option
                if (first?.content != null) {
                    first.content.invoke()
                } else {
                    androidx.compose.material3.Text(
                        text = first?.label ?: "+",
                        style = config.optionTextStyle,
                        color = first?.tint ?: Color.White
                    )
                }
            }
            return@Box
        }

        // expanded: show list of options stacked
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(config.optionSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // show top small header row or handle - keep it minimal
            Box(Modifier.height(6.dp).fillMaxWidth(0.2f).clip(RoundedCornerShape(3.dp)).background(Color.White.copy(alpha = 0.06f)))

            // options
            visibleOptions.forEachIndexed { idx, opt ->
                val optionTint = opt.tint ?: Color.White
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(config.optionHeight)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Button
                        ) {
                            // run option job in coroutine; collapse menu after selection
                            opt.job?.let { jb ->
                                scope.launch { jb() }
                            }
                            // call onSelect-like behavior: collapse
                            expanded = false
                            onOpenChanged?.invoke(false)
                        }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // option content (custom or default text)
                    if (opt.content != null) {
                        opt.content.invoke()
                    } else {
                        androidx.compose.material3.Text(
                            text = opt.label,
                            style = config.optionTextStyle,
                            color = optionTint
                        )
                    }
                }
            }

            // optional footer spacing
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

/** Example usage (nimble):
LiquidMenu(
options = listOf(
LiquidMenuOption(1, "Open", job = { /* suspend navigation */ }),
LiquidMenuOption(2, "Share", job = { /* suspend share */ }),
// up to 10...
),
backdrop = myBackdrop,
config = LiquidMenuConfig()
)
 */
