package com.example.adddataapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adddataapplication.Book
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class AddBookActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AddBookScreen(context = applicationContext){
                    finish()
                }
            }
        }
    }
}

@Composable
fun AddBookScreen(context: Context,onClose: () -> Unit) {
    var bookName by remember { mutableStateOf("") }
    var authorName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Book", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = bookName,
            onValueChange = { bookName = it },
            label = { Text("Book Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = authorName,
            onValueChange = { authorName = it },
            label = { Text("Author Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val newBook = Book(
                    bookName = bookName,
                    authorName = authorName,
                    price = price.toDoubleOrNull() ?: 0.0
                )

                // Update JSON file with new book details
                val gson = Gson()
                val jsonContent = gson.toJson(listOf(newBook))
                writeJsonToFile(context, "books.json", jsonContent)


                // Close the screen
                onClose()


//                finish() // Finish activity and return to previous screen
            }
        ) {
            Text("Add")
        }
    }
}

fun writeJsonToFile(context: Context, fileName: String, jsonContent: String) {
    try {
        val directory = context.filesDir // Internal storage directory
        val file = File(directory, fileName)

        // Read existing JSON content from file
        val existingContent = if (file.exists()) {
            file.readText()
        } else {
            "[]"
        }

        // Parse existing content as JSON array
        val jsonArray = JSONArray(existingContent)

        // Parse new JSON data
        val newData = if (jsonContent.startsWith("[")) {
            JSONArray(jsonContent)
        } else {
            JSONObject(jsonContent)
        }

        // Append new JSON data to the array
        if (newData is JSONArray) {
            for (i in 0 until newData.length()) {
                jsonArray.put(newData.getJSONObject(i))
            }
        } else {
            jsonArray.put(newData)
        }

        // Write the updated JSON array back to the file
        FileOutputStream(file).use { outputStream ->
            outputStream.write(jsonArray.toString().toByteArray())
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview() {
    // Provide a mock context for preview
    val context = LocalContext.current

    // Define a callback to handle closing (you can leave it empty for preview)
    val onClose: () -> Unit = {}

    // Render the AddBookScreen with mock values
    AddBookScreen(context = context, onClose = onClose)
}
