package com.arkeverything.arkdev.arkeasytools.backdrops

import com.kyant.backdrop.Backdrop
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.graphics.GraphicsLayerScope

class BackdropImpl(private val layers: List<BackdropLayer>) : Backdrop {

    override val isCoordinatesDependent: Boolean
        get() = false

    override fun DrawScope.drawBackdrop(
        density: Density,
        coordinates: LayoutCoordinates?,
        layerBlock: (GraphicsLayerScope.() -> Unit)? // <- no default here
    ) {
        // Iterate your layers and apply any custom logic if needed
        layers.forEach { layer ->
            // Normally drawBackdrop DSL handles blur, contrast, etc.
        }
        // You can call layerBlock?.invoke(...) if needed
    }
}

// Extension to convert your LayeredBackdrop into a real Backdrop
fun com.arkeverything.arkdev.arkeasytools.backdrops.LayeredBackdrop.toBackdrop(): Backdrop {
    val kyantLayers = layers.map { layer ->
        BackdropLayer(
            blurRadius = layer.blurRadius,
            contrast = layer.contrast,
            dispersion = layer.dispersion
        )
    }
    return BackdropImpl(kyantLayers)
}
