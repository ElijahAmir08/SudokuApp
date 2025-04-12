package com.example.sudosolver

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import java.io.File

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