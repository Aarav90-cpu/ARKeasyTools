package com.arkstudios.arkeasytools.fromkyant.liquidgl

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkstudios.arkeasytools.fromkyant.BackCapsule.BackCapsule
import com.arkstudios.arkeasytools.ui.liquidglass.catalog.LiquidCatalog
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop


object LiquidGL {

    /** raw backdrop + capsule builder (delegates to BackCapsule object) */
    @Composable
    fun backCapsule(
        modifier: Modifier = Modifier,
        configure: BackCapsule.Builder.() -> Unit = {},
        content: @Composable BoxScope.() -> Unit = {}
    ) {
        BackCapsule(modifier = modifier, configure = configure, content = content)
    }

    /**
     * Catalog scope that automatically creates a LayerBackdrop and forwards it to catalog components.
     * This fixes the "No value passed for parameter 'backdrop'" errors by giving the catalog a valid Backdrop.
     */
    @Composable
    fun catalog(block: @Composable CatalogScope.() -> Unit) {
        // create one shared backdrop for the whole catalog block
        val backdrop: Backdrop = rememberLayerBackdrop()
        val scope = remember { CatalogScope(backdrop) }
        scope.block()
    }

    /**
     * CatalogScope delegates to LiquidCatalog.* functions and passes the shared backdrop automatically.
     * If your LiquidCatalog signatures differ, edit these delegates to match exact parameter names/types.
     */
    class CatalogScope(private val backdrop: Backdrop) {

        @Composable
        fun button(onClick: () -> Unit, content: @Composable RowScope.() -> Unit = {}) {
            LiquidCatalog.liquidButton(backdrop = backdrop, onClick = onClick, content = content)
        }

        @Composable
        fun slider(
            value: Float,
            onValueChange: (Float) -> Unit,
            valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
            visibilityThreshold: Float = 0.01f
        ) {
            LiquidCatalog.liquidSlider(
                value = value,
                onValueChange = onValueChange,
                backdrop = backdrop,
                valueRange = valueRange,
                visibilityThreshold = visibilityThreshold
            )
        }

        @Composable
        fun toggle(selected: Boolean, onSelect: (Boolean) -> Unit) {
            // PASS the Boolean directly (not a lambda)
            LiquidCatalog.liquidToggle(
                selected = selected,      // <-- raw Boolean, not { selected }
                backdrop = backdrop,
                onSelect = onSelect       // use the exact callback name your LiquidCatalog expects
            )
        }

        @Composable
        fun bottomTabs(
            selectedIndex: Int,
            onTabSelected: (Int) -> Unit,
            tabsCount: Int,
            content: @Composable RowScope.() -> Unit = {}
        ) {
            // PASS the Int directly (not a lambda)
            LiquidCatalog.liquidBottomTabs(
                selectedTabIndex = selectedIndex, // <-- raw Int
                onTabSelected = onTabSelected,
                tabsCount = tabsCount,
                backdrop = backdrop,
                content = content
            )
        }
    }
}