package com.example.sudosolver

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.*
import com.example.sudosolver.ui.theme.SudoSolverTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp


class MainActivity : ComponentActivity() {
    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle selected image here (we will display a placeholder for now)
        if (uri != null) {
            // For now, just show a Toast
            Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var puzzleArray by remember { mutableStateOf(Array(9) { IntArray(9) }) }
            var isClueArray by remember { mutableStateOf(Array(9) { BooleanArray(9) }) }

            SudoSolverTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { imagePicker.launch("image/*") }) {
                        Text(text = "Select Sudoku Image")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Image placeholder or selected image will show here
                    Image(
                        painter = painterResource(id = R.drawable.placeholder), // Replace with actual image resource
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        // temporary example puzzle
                        val puzzleStr = "00000000000000102300100450001624370550010720670006500406708300170036802308412607"

                        // Import and solve board
                        val board = SudoBoard()
                        board.fromString(puzzleStr)
                        board.solveCandidates()
                        board.brute(0, 0)

                        // Convert solved board into IntArray and BooleanArray
                        val puzzle = Array(9) { IntArray(9) }
                        val isClue = Array(9) { BooleanArray(9) }

                        for (row in 0..8) {
                            for (col in 0..8) {
                                puzzle[row][col] = board.getCellValue(row, col)
                                isClue[row][col] = board.currBoard[row][col].isClue
                            }
                        }

                        // Store puzzle in state to trigger UI update
                        puzzleArray = puzzle
                        isClueArray = isClue
                    }) {
                        Text(text = "Solve Puzzle")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display Sudoku Grid
                    SudokuGrid(puzzle = puzzleArray, isClue = isClueArray)
                }
            }
        }
    }
}

@Composable
fun SudokuGrid(
    puzzle: Array<IntArray>,
    isClue: Array<BooleanArray>,
    size: Dp = 300.dp
) {
    val cellSize = size / 9

    Canvas(modifier = Modifier.size(size)) {
        for (row in 0..8) {
            for (col in 0..8) {
                val value = puzzle[row][col]
                if (value != 0) {
                    drawContext.canvas.nativeCanvas.drawText(
                        value.toString(),
                        (col * cellSize.toPx()) + cellSize.toPx() / 2,
                        (row * cellSize.toPx()) + cellSize.toPx() * 0.75f,
                        android.graphics.Paint().apply {
                            color = if (isClue[row][col]) android.graphics.Color.BLUE else android.graphics.Color.BLACK
                            textSize = cellSize.toPx() * 0.7f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }
        }

        // Draw grid lines
        for (i in 0..9) {
            val lineThickness = if (i % 3 == 0) 4f else 1f
            drawLine(
                color = Color.Black,
                start = Offset(0f, i * cellSize.toPx()),
                end = Offset(size.toPx(), i * cellSize.toPx()),
                strokeWidth = lineThickness,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.Black,
                start = Offset(i * cellSize.toPx(), 0f),
                end = Offset(i * cellSize.toPx(), size.toPx()),
                strokeWidth = lineThickness,
                cap = StrokeCap.Round
            )
        }
    }
}
