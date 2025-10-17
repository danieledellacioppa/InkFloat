package com.forteur.inkfloat.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.forteur.inkfloat.drawing.DrawingViewModel
import com.forteur.inkfloat.ui.canvas.DrawingSurface
import com.forteur.inkfloat.ui.components.ActionRow
import com.forteur.inkfloat.ui.components.ColorPickerRow
import com.forteur.inkfloat.ui.components.StrokeSlider
import com.forteur.inkfloat.ui.theme.InkFloatTheme

@Composable
fun InkFloatScreen(
    viewModel: DrawingViewModel,
    modifier: Modifier = Modifier
) {
    val strokes = viewModel.strokes
    val strokeWidth = viewModel.strokeWidth
    val selectedColor = viewModel.selectedColor

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = ".:InkFloat:.",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Choose ink color",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        ColorPickerRow(
            selectedColor = selectedColor,
            onColorSelected = viewModel::updateSelectedColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        StrokeSlider(
            strokeWidth = strokeWidth,
            onStrokeWidthChanged = viewModel::updateStrokeWidth
        )

        Spacer(modifier = Modifier.height(16.dp))

        ActionRow(
            onUndo = viewModel::undoLastStroke,
            onClear = viewModel::clearStrokes,
            canUndo = strokes.isNotEmpty(),
            canClear = strokes.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DrawingSurface(
            strokes = strokes,
            selectedColor = selectedColor,
            strokeWidth = strokeWidth,
            onStrokeStart = viewModel::startStroke,
            onStrokeMove = viewModel::addPointToCurrentStroke,
            onStrokeEnd = viewModel::endStroke,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "All drawings stay local to this device." +
                    "\n This app serves no purpose other than trying out different ways to translate calligraphy to a digital medium.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InkFloatScreenPreview() {
    InkFloatTheme {
        val previewViewModel = remember { DrawingViewModel() }
        InkFloatScreen(viewModel = previewViewModel)
    }
}
