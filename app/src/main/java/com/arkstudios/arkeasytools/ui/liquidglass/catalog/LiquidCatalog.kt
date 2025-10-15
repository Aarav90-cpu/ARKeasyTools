package com.arkstudios.arkeasytools.ui.liquidglass.catalog

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.catalog.components.*

object LiquidCatalog {

    @Composable
    fun liquidButton(
        backdrop: Backdrop,
        onClick: () -> Unit,
        content: @Composable (RowScope.() -> Unit)? = null
    ) {
        LiquidButton(
            backdrop = backdrop,
            onClick = onClick,
            content = content ?: {}
        )
    }

    @Composable
    fun liquidSlider(
        value: Float,
        onValueChange: (Float) -> Unit,
        backdrop: Backdrop,
        valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
        visibilityThreshold: Float = 0.01f
    ) {
        LiquidSlider(
            value = { value },
            onValueChange = onValueChange,
            backdrop = backdrop,
            valueRange = valueRange,
            visibilityThreshold = visibilityThreshold
        )
    }

    @Composable
    fun liquidToggle(
        selected: Boolean,
        backdrop: Backdrop,
        onSelect: (Boolean) -> Unit  // use the correct parameter name
    ) {
        LiquidToggle(
            selected = { selected },
            backdrop = backdrop,
            onSelect = onSelect  // matches the library
        )
    }


    @Composable
    fun liquidBottomTabs(
        selectedTabIndex: Int,
        onTabSelected: (Int) -> Unit,
        tabsCount: Int,
        backdrop: Backdrop,
        content: @Composable (RowScope.() -> Unit)? = null
    ) {
        LiquidBottomTabs(
            selectedTabIndex = { selectedTabIndex },
            onTabSelected = onTabSelected,
            tabsCount = tabsCount,
            backdrop = backdrop,
            content = content ?: {}
        )
    }
}
