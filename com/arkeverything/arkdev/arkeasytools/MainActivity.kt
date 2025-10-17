package com.arkeverything.arkdev.arkeasytools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkeverything.arkdev.arkeasytools.LiquidGL.BackdropLayered
import com.arkeverything.arkdev.arkeasytools.LiquidScope
import com.arkeverything.arkdev.arkeasytools.patch.rememberDummyBackdrop
import com.arkeverything.arkdev.arkeasytools.components.*

class LiquidTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // remember a dummy backdrop for components
            val dummyBackdrop = rememberDummyBackdrop()

            // Full backdrop with layered effect
            BackdropLayered(
                modifier = Modifier.fillMaxSize(),
                blurRadius = LiquidScope.liquidModifier.blurRadius,
                tint = Color(0x88FFFFFF)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // --- Liquid Button ---
                    // --- Liquid Button ---
                    LiquidButton(
                        onClick = { println("Button Pressed!") },
                        backdrop = dummyBackdrop
                    ) {
                        Text("Press Me")
                    }

// --- Liquid Slider ---
                    var sliderValue by remember { mutableFloatStateOf(0.5f) }
                    LiquidSlider(
                        value = { sliderValue },
                        onValueChange = { sliderValue = it },
                        valueRange = 0f..1f,
                        backdrop = dummyBackdrop
                    )
                    // optional slider content


// --- Liquid Toggle ---
                    var toggleState by remember { mutableStateOf(false) }
                    LiquidToggle(
                        checked = toggleState,
                        onCheckedChange = { toggleState = it },
                        backdrop = dummyBackdrop
                    )
                    // optional toggle content




// --- Liquid Bottom Tabs ---
                    var selectedTab by remember { mutableIntStateOf(0) }
                    LiquidBottomTabs(
                        selectedTabIndex = { selectedTab },
                        onTabSelected = { selectedTab = it },
                        tabsCount = 3,
                        backdrop = dummyBackdrop
                    )
                    // optional tab content
                    {
                        Text("Tab 1")
                        Text("Tab 2")
                        Text("Tab 3")
                    }
                }
            }
        }
    }
}
