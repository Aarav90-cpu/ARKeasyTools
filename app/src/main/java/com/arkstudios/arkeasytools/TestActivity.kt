package com.arkstudios.arkeasytools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkstudios.arkeasytools.fromkyant.liquidgl.LiquidGL


class LiquidCatalogTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Track UI states
            var toggleState by remember { mutableStateOf(false) }
            var sliderValue by remember { mutableFloatStateOf(0.5f) }
            var selectedTab by remember { mutableIntStateOf(0) }

            LiquidGL.catalog {
                // Button example
                button(onClick = { println("Button pressed!") }) {
                    Text("Press Me", modifier = Modifier.padding(8.dp))
                }

                // Slider example
                slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = 0f..1f
                )

                // Toggle example
                toggle(selected = toggleState, onSelect = { toggleState = it })

                // Bottom Tabs example
                bottomTabs(
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it },
                    tabsCount = 3
                ) {
                    Row {
                        Text("Tab 1", modifier = Modifier.padding(4.dp))
                        Text("Tab 2", modifier = Modifier.padding(4.dp))
                        Text("Tab 3", modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}
