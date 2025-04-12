package com.example.sudosolver.ui.theme
import android.graphics.Bitmap

object ImageSlicer {
    //Split the extracted bitMap into 81 cells. Must assume the input is a perfect square and has the whole grid
    fun sliceSudokuBitMap (bitmap: Bitmap) : List<Bitmap> {
        val cellBitmaps = mutableListOf<Bitmap> ()

        val height = bitmap.height
        val width = bitmap.width

        //Ensure this is a perfect square
        require(width == height) {"Sudoku image must be a perfect square" }
            val cellSize = width /9
            for (row in 0 until 9) {
                for (col in 0 until 9) {
                    val x = col * cellSize
                    val y = row * cellSize
                    //Crop each of these cells
                    val margin = cellSize / 10 //Padding for reading
                    val cellBitmap = Bitmap.createBitmap(bitmap, x + margin, y + margin, cellSize - 2*margin, cellSize - 2*margin)
                    cellBitmaps.add(cellBitmap)
                }
            }
        return cellBitmaps
        }
}