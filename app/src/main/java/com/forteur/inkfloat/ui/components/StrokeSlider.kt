package com.forteur.inkfloat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun StrokeSlider(
    strokeWidth: Float,
    onStrokeWidthChanged: (Float) -> Unit
) {
    Column {
        Text(
            text = "Stroke width: ${strokeWidth.toInt()} dp",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Slider(
            value = strokeWidth,
            onValueChange = onStrokeWidthChanged,
            valueRange = 4f..36f
        )
    }
}
