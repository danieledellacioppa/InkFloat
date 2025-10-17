package com.forteur.inkfloat.drawing

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.forteur.inkfloat.model.Stroke

class DrawingViewModel : ViewModel() {
    private val _strokes = mutableStateListOf<Stroke>()
    val strokes: List<Stroke> get() = _strokes

    var strokeWidth by mutableFloatStateOf(12f)
        private set

    var selectedColor by mutableStateOf(Color.Black)
        private set

    private var activeStroke: Stroke? = null

    fun updateSelectedColor(color: Color) {
        selectedColor = color
    }

    fun updateStrokeWidth(width: Float) {
        strokeWidth = width
    }

    fun startStroke(startPoint: Offset, strokeWidthPx: Float) {
        val stroke = Stroke(
            color = selectedColor,
            strokeWidthPx = strokeWidthPx
        ).apply {
            points.add(startPoint)
        }
        _strokes.add(stroke)
        activeStroke = stroke
    }

    fun addPointToCurrentStroke(point: Offset) {
        activeStroke?.points?.add(point)
    }

    fun endStroke() {
        activeStroke = null
    }

    fun undoLastStroke() {
        if (_strokes.isNotEmpty()) {
            _strokes.removeAt(_strokes.lastIndex)
            activeStroke = null
            Log.d("DrawingViewModel", "Undo last stroke, remaining strokes: ${_strokes.size}")
        }
    }

    fun clearStrokes() {
        _strokes.clear()
        activeStroke = null
    }
}
