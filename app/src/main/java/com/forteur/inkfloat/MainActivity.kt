package com.forteur.inkfloat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forteur.inkfloat.ui.theme.InkFloatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InkFloatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InkFloatScreen()
                }
            }
        }
    }
}

data class Stroke(
    val color: Color,
    val strokeWidthPx: Float,
    val points: SnapshotStateList<Offset> = mutableStateListOf()
)

@Composable
fun InkFloatScreen(modifier: Modifier = Modifier) {
    val strokes = remember { mutableStateListOf<Stroke>() }
    var strokeWidth by remember { mutableFloatStateOf(12f) }
    var selectedColor by remember { mutableStateOf(Color.Black) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = "InkFloat",
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
            onColorSelected = { selectedColor = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        StrokeSlider(
            strokeWidth = strokeWidth,
            onStrokeWidthChanged = { strokeWidth = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ActionRow(
            onUndo = {
                if (strokes.isNotEmpty()) {
                    strokes.removeAt(strokes.lastIndex)
                }
            },
            onClear = { strokes.clear() },
            canUndo = strokes.isNotEmpty(),
            canClear = strokes.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DrawingSurface(
            strokes = strokes,
            selectedColor = selectedColor,
            strokeWidth = strokeWidth,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "All drawings stay local to this device.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DrawingSurface(
    strokes: SnapshotStateList<Stroke>,
    selectedColor: Color,
    strokeWidth: Float,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var activeStroke by remember { mutableStateOf<Stroke?>(null) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(selectedColor, strokeWidth) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val stroke = Stroke(
                                color = selectedColor,
                                strokeWidthPx = with(density) { strokeWidth.dp.toPx() }
                            ).apply {
                                points.add(offset)
                            }
                            strokes.add(stroke)
                            activeStroke = stroke
                        },
                        onDrag = { change, _ ->
                            activeStroke?.points?.add(change.position)
                        },
                        onDragCancel = { activeStroke = null },
                        onDragEnd = { activeStroke = null }
                    )
                }
        ) {
            strokes.forEach { stroke ->
                val points = stroke.points
                if (points.isEmpty()) return@forEach

                if (points.size == 1) {
                    drawCircle(
                        color = stroke.color,
                        radius = stroke.strokeWidthPx / 2f,
                        center = points.first()
                    )
                } else {
                    for (index in 0 until points.size - 1) {
                        drawLine(
                            color = stroke.color,
                            start = points[index],
                            end = points[index + 1],
                            strokeWidth = stroke.strokeWidthPx,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorPickerRow(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val colors = listOf(
        Color.Black,
        Color(0xFFBF360C),
        Color(0xFF0D47A1),
        Color(0xFF1B5E20),
        Color(0xFF6A1B9A),
        Color(0xFFFBC02D),
        Color(0xFF546E7A)
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        colors.forEach { color ->
            ColorChoice(
                color = color,
                isSelected = color == selectedColor,
                onClick = { onColorSelected(color) }
            )
        }
    }
}

@Composable
private fun ColorChoice(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderWidth = if (isSelected) 3.dp else 1.dp
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(color)
            .border(borderWidth, borderColor, CircleShape)
            .clickable(onClick = onClick)
    )
}

@Composable
private fun StrokeSlider(
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

@Composable
private fun ActionRow(
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InkFloatScreenPreview() {
    InkFloatTheme {
        InkFloatScreen()
    }
}
