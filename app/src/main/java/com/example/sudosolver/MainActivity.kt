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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.draw.clip
import com.example.sudosolver.ui.theme.SudoSolverTheme


class MainActivity : ComponentActivity() {
    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle selected image here (we will display a placeholder for now)
        if (uri != null) {
            // For now, just show a Toast
            Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show()
        }
    }
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.getStringExtra("captured_image_uri")
        if (uri != null) {
            Toast.makeText(this, "Camera image received!", Toast.LENGTH_SHORT).show()
            // TODO: Use the URI for OCR/preview
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                        // TODO: Implement solve puzzle functionality
                    }) {
                        Text(text = "Solve Sudoku")
                    }
                }
            }
        }
    }

}
