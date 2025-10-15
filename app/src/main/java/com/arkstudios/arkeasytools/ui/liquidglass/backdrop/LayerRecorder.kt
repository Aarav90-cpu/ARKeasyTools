package com.kyant.backdrop

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.requireDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntSize

// simpler, stable-friendly implementation without context parameters
fun recordLayer(
    drawScope: DrawScope,
    node: DelegatableNode,
    layer: GraphicsLayer,
    size: IntSize = drawScope.size.toIntSize(),
    block: DrawScope.() -> Unit
) {
    layer.record(
        density = node.requireDensity(),
        layoutDirection = drawScope.layoutDirection,
        size = size
    ) {
        drawScope.draw(
            density = drawScope.drawContext.density,
            layoutDirection = drawScope.drawContext.layoutDirection,
            canvas = drawScope.drawContext.canvas,
            size = drawScope.drawContext.size,
            graphicsLayer = drawScope.drawContext.graphicsLayer,
            block = block
        )
    }
}
