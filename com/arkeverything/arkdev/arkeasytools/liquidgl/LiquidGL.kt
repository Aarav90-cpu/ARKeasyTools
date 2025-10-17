package com.arkeverything.arkdev.arkeasytools.liquidgl

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkeverything.arkdev.arkeasytools.liquidgl.components.*
import com.arkeverything.arkdev.arkeasytools.liquidgl.dummyBackdrop.DummyBackdrop
import androidx.compose.foundation.layout.RowScope
import com.arkeverything.arkdev.arkeasytools.liquidgl.assets.LiquidGLConfig
import com.arkeverything.arkdev.arkeasytools.liquidgl.assets.LiquidBottomTabs
import android.R.attr.content

object LiquidGL {

    // --- GLOBAL CONFIGS ---
    val config get() = LiquidGLConfig.get()
    var tint by mutableStateOf(LiquidGLConfig.defaultTint)
    var paddingDp by mutableStateOf(config.buttonPadding)
    var buttonSize by mutableStateOf(config.buttonSize)

    // --- BUTTON ---
    @Composable
    fun Btn(
        modifier: Modifier = Modifier,
        job: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        LiquidButton(
            backdrop = DummyBackdrop,
            config = LiquidButtonConfig(),
            modifier = modifier,
            job = job
        ) { content() }
    }

    // --- TOGGLE ---
    @Composable
    fun Tgl(
        selected: () -> Boolean,
        onSelect: (Boolean) -> Unit,
        job: (() -> Unit)? = null,
        modifier: Modifier = Modifier
    ) {
        LiquidToggle(
            backdrop = DummyBackdrop,
            selected = selected,
            onSelect = {
                onSelect(it)
                job?.invoke()
            },
            modifier = modifier,
            config = ToggleConfig()
        )
    }

    // --- SLIDER ---
    @Composable
    fun Sld(
        value: () -> Float,
        onValueChange: (Float) -> Unit,
        valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
        job: ((Float) -> Unit)? = null,
        modifier: Modifier = Modifier
    ) {
        LiquidSlider(
            backdrop = DummyBackdrop,
            value = value,
            onValueChange = {
                onValueChange(it)
                job?.invoke(it)
            },
            valueRange = valueRange,
            modifier = modifier,
            config = LiquidSliderConfig()
        )
    }

    // --- BOTTOM TABS ---
    @Composable
    fun Tabs(
        count: Int,
        selectedTabIndex: Int = 0,
        onTabSelected: (Int) -> Unit = {},
        modifier: Modifier = Modifier,
        content: @Composable RowScope.() -> Unit = { Text("Tab") }
    ) {
        LiquidBottomTabs(
            tabsCount = count,
            selectedTabIndex = { selectedTabIndex },
            onTabSelected = onTabSelected,
            modifier = modifier,
            backdrop = DummyBackdrop,
            content = content
        )
    }


    // --- MENU ---
    @Composable
    fun Menu(
        options: List<LiquidMenuOption>,
        modifier: Modifier = Modifier,
        initiallyExpanded: Boolean = false,
        onOpenChanged: ((Boolean) -> Unit)? = null
    ) {
        LiquidMenu(
            options = options,
            backdrop = DummyBackdrop,
            modifier = modifier,
            config = LiquidMenuConfig(
                circleSize = 56.dp,
                optionHeight = 44.dp,
                optionSpacing = 6.dp,
                baseWidth = 140.dp,
                extraWidthPerOption = 8.dp,
                blurDp = 8.dp,
                lensXDp = 12.dp,
                lensYDp = 12.dp,
                backgroundTint = Color.White.copy(alpha = 0.06f)
            ),
            initiallyExpanded = initiallyExpanded,
            onOpenChanged = onOpenChanged
        )
    }

    // --- MODAL ---
    @Composable
    fun Modal(
        show: Boolean,
        onDismiss: () -> Unit,
        content: @Composable () -> Unit
    ) {
        if (show) Box(Modifier.fillMaxSize().padding(paddingDp)) { content() }
    }

    // --- TOAST ---
    @Composable
    fun Toast(
        msg: String,
        durationMs: Long = 2000
    ) {
        BasicText(msg)
    }

    // --- FULL GOD MODE SCREEN ---
    @Composable
    fun FullScreen(
        tabsCount: Int = 5,
        selectedTab: Int = 0,
        onTabSelected: (Int) -> Unit = {},
        sliderValue: MutableState<Float> = mutableFloatStateOf(0f),
        toggleValue: MutableState<Boolean> = mutableStateOf(false),
        showButton: Boolean = true,
        menuOptions: List<LiquidMenuOption> = emptyList(),
        showMenu: Boolean = false,
        showModal: Boolean = false,
        modalContent: @Composable () -> Unit = {}
    ) {
        Column(Modifier.fillMaxSize().padding(paddingDp)) {

            Tabs(
                count = tabsCount,
                selectedTabIndex = selectedTab,
                onTabSelected = onTabSelected
            )

            Spacer(Modifier.height(16.dp))

            Sld(value = { sliderValue.value }, onValueChange = { sliderValue.value = it })

            Spacer(Modifier.height(16.dp))

            Tgl(selected = { toggleValue.value }, onSelect = { toggleValue.value = it })

            Spacer(Modifier.height(16.dp))

            if (showButton) {
                Btn(job = { /* do something */ }) { Text("Button") }
            }

            Spacer(Modifier.height(16.dp))

            if (showMenu) {
                Menu(options = menuOptions)
            }

            Modal(show = showModal, onDismiss = {}) { modalContent() }
        }
    }
}
