package com.forteur.inkfloat.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.forteur.inkfloat.model.Stroke

@Composable
fun DrawingSurface(
    strokes: List<Stroke>,
    selectedColor: Color,
    strokeWidth: Float,
    onStrokeStart: (Offset, Float) -> Unit,
    onStrokeMove: (Offset) -> Unit,
    onStrokeEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

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
                            val strokeWidthPx = with(density) { strokeWidth.dp.toPx() }
                            onStrokeStart(offset, strokeWidthPx)
                        },
                        onDrag = { change, _ ->
                            onStrokeMove(change.position)
                        },
                        onDragCancel = { onStrokeEnd() },
                        onDragEnd = { onStrokeEnd() }
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
