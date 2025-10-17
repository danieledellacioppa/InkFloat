package com.forteur.inkfloat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ActionRow(
    onUndo: () -> Unit,
    onClear: () -> Unit,
    canUndo: Boolean,
    canClear: Boolean
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (undoRef, clearRef) = createRefs()
        val guideline = createGuidelineFromStart(0.5f)

        OutlinedButton(
            onClick = onUndo,
            enabled = canUndo,
            modifier = Modifier.constrainAs(undoRef) {
                start.linkTo(parent.start)
                end.linkTo(guideline)
                width = androidx.constraintlayout.compose.Dimension.fillToConstraints
            }
        ) { Text("Undo") }

        Button(
            onClick = onClear,
            enabled = canClear,
            modifier = Modifier.constrainAs(clearRef) {
                start.linkTo(guideline, margin = 12.dp)
                end.linkTo(parent.end)
                width = androidx.constraintlayout.compose.Dimension.fillToConstraints
            }
        ) { Text("Clear") }
    }
}
