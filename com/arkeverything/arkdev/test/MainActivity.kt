package com.arkeverything.arkdev.arkeasytools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkeverything.arkdev.arkeasytools.liquidgl.LiquidGL
import com.arkeverything.arkdev.arkeasytools.liquidgl.components.LiquidBottomTabs
import com.arkeverything.arkdev.arkeasytools.liquidgl.components.LiquidTabsConfig
import com.kyant.backdrop.Backdrop

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BottomTabs()
                }
            }
        }
    }
}

@Composable
fun BottomTabs(
    backdrop: Backdrop?, // <- make nullable
    tabsCount: Int = 3,
    modifier: Modifier = Modifier,
    config: LiquidTabsConfig = LiquidTabsConfig(),
    job: ((Int) -> Unit)? = null,
    content: @Composable RowScope.(Int) -> Unit
) {
    if (backdrop != null) {
        LiquidBottomTabs(
            backdrop = backdrop,
            tabsCount = tabsCount,
            modifier = modifier,
            config = config,
            job = job,
            content = content
        )
    } else {
        // fallback for viewing only
        // maybe just a Row with Texts for tabs
        androidx.compose.foundation.layout.Row(modifier = modifier) {
            for (i in 0 until tabsCount) {
                androidx.compose.material3.Text(
                    text = "Tab $i",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun RowScope.TabContent(index: Int) {
    Text(text = "Tab $index content")
}
