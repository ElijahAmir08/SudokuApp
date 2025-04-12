package com.example.sudosolver

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    fun copyTrainedDataFromAssets(context: Context, languageCode: String) {
        val assetManager = context.assets
        val inputStream = assetManager.open("tessdata/$languageCode.traineddata")

        val tessDataDir = File(context.filesDir, "tessdata")
        if (!tessDataDir.exists()) {
            tessDataDir.mkdirs()
        }

        val outFile = File(tessDataDir, "$languageCode.traineddata")
        if (!outFile.exists()) {
            val outputStream = FileOutputStream(outFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.flush()
            outputStream.close()
        }
    }
}
