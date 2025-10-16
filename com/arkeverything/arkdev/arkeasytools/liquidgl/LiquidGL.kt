package com.arkeverything.arkdev.arkeasytools.liquidgl

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kyant.backdrop.Backdrop
import com.arkeverything.arkdev.arkeasytools.liquidgl.components.*

object LiquidGL {

    @Composable
    fun LiquidButtonConfig(
        backdrop: Backdrop,
        config: LiquidButtonConfig = LiquidButtonConfig(),
        modifier: Modifier = Modifier,
        job: (() -> Unit)? = null,
        onClick: (() -> Unit)? = null,
        content: @Composable RowScope.() -> Unit
    ) {
        LiquidButton(
            backdrop = backdrop,
            config = config,
            modifier = modifier,
            job = {
                onClick?.invoke()
                job?.invoke()
            },
            content = content
        )
    }

    @Composable
    fun ToogleConfig(
        backdrop: Backdrop,
        selected: () -> Boolean,
        onSelect: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
        config: ToggleConfig = ToggleConfig(),
        job: (() -> Unit)? = null
    ) {
        LiquidToggle(
            backdrop = backdrop,
            selected = selected,
            onSelect = {
                onSelect(it)
                job?.invoke()
            },
            modifier = modifier,
            config = config
        )
    }

    @Composable
    fun LiquidSlider(
        backdrop: Backdrop,
        value: () -> Float,
        onValueChange: (Float) -> Unit,
        valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
        modifier: Modifier = Modifier,
        config: LiquidSliderConfig = LiquidSliderConfig(),
        job: ((Float) -> Unit)? = null
    ) {
        LiquidSlider(
            backdrop = backdrop,
            value = value,
            onValueChange = {
                onValueChange(it)
                job?.invoke(it)
            },
            valueRange = valueRange,
            modifier = modifier,
            config = config
        )
    }

    @Composable
    fun LiquidTabsConfig(
        backdrop: Backdrop,
        tabsCount: Int = 3,
        modifier: Modifier = Modifier,
        config: LiquidTabsConfig = LiquidTabsConfig(),
        job: ((Int) -> Unit)? = null,
        content: @Composable RowScope.(Int) -> Unit
    ) {
        LiquidTabsConfig(
            backdrop = backdrop,
            tabsCount = tabsCount,
            modifier = modifier,
            config = config,
            job = job,
            content = content
        )
    }

    @Composable
    fun LiquidMenuConfig(
        backdrop: Backdrop,
        options: List<LiquidMenuOption>,
        modifier: Modifier = Modifier,
        config: LiquidMenuConfig = LiquidMenuConfig()
    ) {
        LiquidMenuConfig(
            backdrop = backdrop,
            options = options,
            modifier = modifier,
            config = config
        )
    }
}