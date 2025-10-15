package com.arkstudios.arkeasytools.ui.liquidglass.capsule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule

object LiquidCapsule {

    @Composable
    fun liquidButton(size: Dp, onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .size(size)
                .background(Color.Blue, ContinuousCapsule)
                .clickable { onClick() }
        )
    }

    @Composable
    fun liquidToggle(selected: Boolean, onToggle: (Boolean) -> Unit) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(if (selected) Color.Green else Color.Gray, ContinuousCapsule)
                .clickable { onToggle(!selected) }
        )
    }

    @Composable
    fun liquidSlider(value: Float, onValueChange: (Float) -> Unit) {
        // simple horizontal slider with capsule shape
        Box(
            modifier = Modifier
                .size(200.dp, 24.dp)
                .background(Color.LightGray, ContinuousCapsule)
                .clickable { onValueChange(value) }
        )
    }

    @Composable
    fun liquidCapsuleContainer(content: @Composable () -> Unit) {
        Box(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.2f), ContinuousCapsule)
        ) {
            content()
        }
    }
}
