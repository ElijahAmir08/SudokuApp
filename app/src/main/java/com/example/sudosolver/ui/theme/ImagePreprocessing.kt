package com.example.sudosolver.ui.theme

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.graphics.createBitmap

object ImagePreprocessing {
    fun preprocessCellBitmap(input: Bitmap): Bitmap {
        val width = input.width
        val height = input.height
        val processed = createBitmap(width, height)

        val canvas = Canvas(processed)
        val paint = Paint()
        val contrast = 2.0f  // Boosts contrast
        val brightness = -100f  // Lower brightness slightly

        val colorMatrix = android.graphics.ColorMatrix(floatArrayOf(
            contrast, 0f, 0f, 0f, brightness,
            0f, contrast, 0f, 0f, brightness,
            0f, 0f, contrast, 0f, brightness,
            0f, 0f, 0f, 1f, 0f
        ))

        paint.colorFilter = android.graphics.ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(input, 0f, 0f, paint)

        return processed
    }
}