package com.arkeverything.arkdev.arkeasytools.liquidgl.dummyBackdrop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

// Singleton dummy backdrop for LiquidGL
object DummyBackdrop {

    // Store active tab index
    var activeTab = mutableStateOf(0)

    fun setActiveTab(index: Int) {
        activeTab.value = index
    }

    fun getActiveTab(): Int = activeTab.value
}

// Factory function to mimic rememberBackdropState
@Composable
fun rememberDummyBackdrop(): DummyBackdrop {
    return DummyBackdrop
}