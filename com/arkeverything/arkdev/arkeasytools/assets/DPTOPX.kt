package com.arkeverything.arkdev.arkeasytools.assets

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Dp.toPx(): Float = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
fun Float.toPx(): Float = with(LocalDensity.current) { this@toPx.dp.toPx() }
