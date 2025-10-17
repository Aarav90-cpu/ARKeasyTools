package com.arkeverything.arkdev.arkeasytools.assets

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class DampedDragAnimation(
    private val animationScope: CoroutineScope,
    initialValue: Float,
    val valueRange: ClosedFloatingPointRange<Float>,
    val onDrag: ((Float) -> Unit)? = null,
    val onDragStopped: ((Float) -> Unit)? = null
) {
    var targetValue by mutableStateOf(initialValue)
        private set

    var progress by mutableStateOf((initialValue - valueRange.start) / (valueRange.endInclusive - valueRange.start))
        private set

    var pressProgress by mutableStateOf(0f)
        private set

    fun updateValue(newValue: Float) {
        val clamped = newValue.coerceIn(valueRange)
        targetValue = clamped
        progress = (clamped - valueRange.start) / (valueRange.endInclusive - valueRange.start)
    }

    fun animateToValue(newValue: Float) {
        animationScope.launch {
            updateValue(newValue)
            onDragStopped?.invoke(targetValue)
        }
    }

    fun drag(delta: Float) {
        val range = valueRange.endInclusive - valueRange.start
        updateValue(targetValue + delta * range)
        onDrag?.invoke(targetValue)
    }
}
