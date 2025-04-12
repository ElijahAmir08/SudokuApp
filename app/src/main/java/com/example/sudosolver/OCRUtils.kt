package com.example.sudosolver

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.sudosolver.ui.theme.ImagePreprocessing
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import java.io.File
import androidx.core.graphics.scale

object OCRUtils {

    suspend fun extractDigitsFromBitmap(bitmap: Bitmap): String {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        return try {
            val result = recognizer.process(image).await()
            val allText = result.text
            Log.d("OCR", "Raw OCR output: $allText")

            // This is a basic placeholder: clean up non-digits
            allText.filter { it in '1'..'9' || it == '.' || it == '0' }

        } catch (e: Exception) {
            Log.e("OCR", "OCR failed: ${e.message}")
            ""
        }
    }

    suspend fun extractDigitsCells(cells: List<Bitmap>): String { //Creates empty list
        //I need to loop through each cell and feed it to the ML kit
        //If it detects a number 1 - 9 use it, but if unsure return '.'
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val result = StringBuilder()
        for ((index, cell) in cells.withIndex()) { //Loop through the cells and their indexes [0 to 80]
            val preprocessed = ImagePreprocessing.preprocessCellBitmap(cell)
            val scaled = preprocessed.scale(50, 50)
            val image = InputImage.fromBitmap(scaled, 0)

            try {
                val text = recognizer.process(image).await().text
                //If it is any character except for 1 to 9 then return '.' & Ensures it reads one char at a time
                val digit = text.trim().firstOrNull {it in '1'..'9'}?: '.'
                result.append(digit)
                Log.d("OCR Raw", "Cell $index raw text: '$text'")
            } catch (e : Exception) {
                result.append('.')
                Log.e("OCR error", "Cell $index: ${e.message}")
            }
        }
        return result.toString()
    }

    /*fun normalizeDigit(text: String): Char {
        return when (text.trim()) {
            "L", "l" -> '5'
            "S", "s" -> '5'
            "O", "o" -> '0'
            "Z", "z" -> '2'
            else -> text.firstOrNull { it in '1'..'9' } ?: '.'
        }
    }*/

    fun saveTextToFile(context: Context, content: String, label: String = "puzzle"): File? {
        return try {
            val file = File(context.filesDir, "$label.txt")
            file.writeText(content)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}