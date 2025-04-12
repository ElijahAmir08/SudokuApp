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
                    val cellBitmap = Bitmap.createBitmap(bitmap, x, y, cellSize, cellSize)
                    cellBitmaps.add(cellBitmap)
                }
            }
        return cellBitmaps
        }
}