package com.forteur.inkfloat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionRow(
    onUndo: () -> Unit,
    onClear: () -> Unit,
    canUndo: Boolean,
    canClear: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = onUndo,
            enabled = canUndo,
            modifier = Modifier.weight(1f)
        ) {
            Text("Undo")
        }
        Button(
            onClick = onClear,
            enabled = canClear,
            modifier = Modifier.weight(1f)
        ) {
            Text("Clear")
        }
    }
}
