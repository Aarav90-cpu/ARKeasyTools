package com.arkeverything.arkdev.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkeverything.arkdev.arkeasytools.liquidgl.LiquidGL

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // State holders
            val sliderValue = remember { mutableFloatStateOf(0.5f) }
            val toggleValue = remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                // --- Slider controlling the tint ---
                LiquidGL.Sld(
                    value = { sliderValue.value },
                    onValueChange = {
                        sliderValue.value = it
                        LiquidGL.tint = Color(
                            red = it,
                            green = 0.3f,
                            blue = 1f - it
                        ) // dynamic tint
                    },
                    job = { println("Slider moved: $it") }
                )

                // --- Toggle ---
                LiquidGL.Tgl(
                    selected = { toggleValue.value },
                    onSelect = { toggleValue.value = it },
                    job = { println("Toggle is now $toggleValue") }
                )

                // --- Button using current tint ---
                LiquidGL.Btn(
                    job = { println("Button pressed!") }
                ) {
                    Text(text = "Liquid Button")
                }
            }
        }
    }
}
