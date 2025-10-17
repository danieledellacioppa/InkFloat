package com.forteur.inkfloat.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Stroke(
    val color: Color,
    val strokeWidthPx: Float,
    val points: SnapshotStateList<Offset> = mutableStateListOf()
)