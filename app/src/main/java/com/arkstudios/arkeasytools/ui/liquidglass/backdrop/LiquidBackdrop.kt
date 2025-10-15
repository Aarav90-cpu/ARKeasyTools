package com.arkstudios.arkeasytools.ui.liquidglass.backdrop

import androidx.compose.runtime.Composable
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.InnerShadow
import com.kyant.backdrop.shadow.Shadow

@Composable
fun LiquidBackdropWrapper(
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.drawBackdrop(
            backdrop = backdrop,
            shape = { androidx.compose.foundation.shape.RoundedCornerShape(16.dp) }, // <-- Add this
            effects = {
                blur(8f.dp.toPx())
                lens(12f.dp.toPx(), 12f.dp.toPx())
            },
            highlight = { Highlight.Default },
            shadow = { Shadow(radius = 4f.dp, color = Color.Black.copy(alpha = 0.05f)) },
            innerShadow = { InnerShadow(radius = 4f.dp, alpha = 0.1f) }
        )
    ) {
        content()
    }
}