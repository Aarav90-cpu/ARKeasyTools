package com.arkeverything.arkdev.arkeasytools.patch

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.kyant.backdrop.Backdrop
import androidx.compose.ui.graphics.GraphicsLayerScope

/**
 * Minimal dummy implementation of Backdrop to satisfy the interface.
 */
class DummyBackdropInstance : Backdrop {
    override val isCoordinatesDependent: Boolean
        get() = false

    override fun DrawScope.drawBackdrop(
        density: Density,
        coordinates: LayoutCoordinates?,
        layerBlock: (GraphicsLayerScope.() -> Unit)?
    ) {
        // Minimal drawing: do nothing
    }
}

/**
 * Helper to safely remember a dummy backdrop inside composables
 */
@Composable
fun rememberDummyBackdrop(): Backdrop {
    return remember { DummyBackdropInstance() }
}

/**
 * Optional helpers for default values
 */
fun Float?.orDefault(default: Float): Float = this ?: default
fun Color?.orDefault(default: Color): Color = this ?: default
