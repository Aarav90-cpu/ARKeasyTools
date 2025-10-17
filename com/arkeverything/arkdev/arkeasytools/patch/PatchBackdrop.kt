package com.arkeverything.arkdev.arkeasytools.patch

import com.kyant.backdrop.Backdrop
import com.arkeverything.arkdev.arkeasytools.backdrops.BackdropImpl
import com.arkeverything.arkdev.arkeasytools.backdrops.BackdropLayer
import com.arkeverything.arkdev.arkeasytools.backdrops.LayeredBackdrop

// Converts your custom LayeredBackdrop into a real Kyant Backdrop
fun LayeredBackdrop.toBackdrop(): Backdrop {
    val kyantLayers = layers.map { layer ->
        BackdropLayer(
            blurRadius = layer.blurRadius,
            contrast = layer.contrast,
            dispersion = layer.dispersion
        )
    }
    return BackdropImpl(kyantLayers)
}
