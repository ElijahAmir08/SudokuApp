package com.example.sudosolver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.sudosolver.ui.theme.SudoSolverTheme
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {

    private val imageUri = mutableStateOf<Uri?>(null)

    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri.value = uri
            Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.getStringExtra("captured_image_uri")
        if (uri != null) {
            imageUri.value = Uri.parse(uri)
            Toast.makeText(this, "Camera image received!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudoSolverTheme {
                val context = LocalContext.current
                val currentImageUri = imageUri.value

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        cameraLauncher.launch(Intent(context, CameraCaptureActivity::class.java))
                    }) {
                        Text("Capture Sudoku Image")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { imagePicker.launch("image/*") }) {
                        Text(text = "Select Sudoku Image")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (currentImageUri != null) {
                        AsyncImage(
                            model = currentImageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.placeholder),
                            contentDescription = "Placeholder Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        currentImageUri?.let { uri ->
                            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                            Toast.makeText(context, "Bitmap extracted: ${bitmap.width}x${bitmap.height}", Toast.LENGTH_SHORT).show()
                            lifecycleScope.launch {
                                val puzzleString = OCRUtils.extractDigitsFromBitmap(bitmap)
                                Toast.makeText(context, "Extracted: $puzzleString", Toast.LENGTH_LONG).show()
                                OCRUtils.saveTextToFile(context, puzzleString)
                            }
                        }
                    }) {
                        Text(text = "Solve Sudoku")
                    }
                }
            }
        }
    }
}