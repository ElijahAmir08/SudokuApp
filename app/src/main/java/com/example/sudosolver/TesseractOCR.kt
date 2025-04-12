package com.example.sudosolver
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.googlecode.tesseract.android.TessBaseAPI

object TesseractOCR {

    private var tess: TessBaseAPI? = null

    /**
     * Initializes Tesseract with digit-only config and assets path
     */
    fun init(context: Context) {
        if (tess != null) return // already initialized

        val dataPath = context.filesDir.absolutePath
        val tessDataPath = "$dataPath/tessdata"

        // Copy traineddata file from assets to internal storage
        FileUtils.copyTrainedDataFromAssets(context, "digits")

        tess = TessBaseAPI()
        val success = tess!!.init(dataPath, "digits")

        if (!success) {
            Log.e("TesseractOCR", "Tesseract initialization failed")
        } else {
            tess!!.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "123456789")
            tess!!.pageSegMode = TessBaseAPI.PageSegMode.PSM_SINGLE_CHAR
        }
    }

    /**
     * Runs OCR on a given Bitmap cell and returns a digit or '.'
     */
    fun recognizeDigit(cellBitmap: Bitmap): Char {
        if (tess == null) return '.'

        tess!!.setImage(cellBitmap)
        val text = tess!!.utF8Text.trim()
        val digit = text.firstOrNull { it in '1'..'9' } ?: '.'
        Log.d("TesseractOCR", "OCR result: '$text' → '$digit'")

        return digit
    }

    fun shutdown() {
        tess?.end()
        tess = null
    }
}