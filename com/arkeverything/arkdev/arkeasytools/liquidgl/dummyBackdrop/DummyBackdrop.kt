package com.arkeverything.arkdev.arkeasytools.liquidgl.dummyBackdrop

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Density
import com.kyant.backdrop.Backdrop
import androidx.compose.ui.graphics.GraphicsLayerScope

object DummyBackdrop : Backdrop {

    // Optional: store toggle/slider states for testing
    private val toggleStates = mutableMapOf<String, Boolean>()
    private val sliderValues = mutableMapOf<String, Float>()

    override val isCoordinatesDependent: Boolean
        get() = false

    override fun DrawScope.drawBackdrop(
        density: Density,
        coordinates: LayoutCoordinates?,
        layerBlock: (GraphicsLayerScope.() -> Unit)?
    ) {
        // do nothing, this is just dummy
    }

    // Optional: helpers to store states
    fun getToggleState(key: String, default: Boolean = false) = toggleStates.getOrDefault(key, default)
    fun setToggleState(key: String, value: Boolean) { toggleStates[key] = value }

    fun getSliderValue(key: String, default: Float = 0f) = sliderValues.getOrDefault(key, default)
    fun setSliderValue(key: String, value: Float) { sliderValues[key] = value.coerceIn(0f, 1f) }

    fun resetAll() {
        toggleStates.clear()
        sliderValues.clear()
    }
}

