package com.example.adddataapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import com.example.adddataapplication.AddBookActivity
import com.example.adddataapplication.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileNotFoundException
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val gson = Gson()
            val fileName = "books.json"
            val books = remember { mutableStateListOf<Book>() }

            // Read existing book list from JSON file
            val existingBooks = readBooksFromJson(applicationContext, fileName, gson)
            books.addAll(existingBooks)

            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Book List") }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                startActivity(Intent(this@MainActivity, AddBookActivity::class.java))
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                ) {
                    BookList(books)
                }
            }
        }
    }
}

@Composable
fun BookList(books: List<Book>) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(books) { book ->
            BookCard(book)
        }
    }
}

@Composable
fun BookCard(book: Book) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Book Name: ${book.bookName}")
            Text(text = "Author Name: ${book.authorName}")
            Text(text = "Price: $${book.price}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookListPreview() {
    val sampleBooks = listOf(
        Book("Book 1", "Author 1", 19.99),
        Book("Book 2", "Author 2", 24.99),
        Book("Book 3", "Author 3", 14.99)
    )
    BookList(books = sampleBooks)
}


fun readBooksFromJson(context: Context, fileName: String, gson: Gson): List<Book> {
    return try {
        val inputStream = context.openFileInput(fileName)
        val listType = object : TypeToken<List<Book>>() {}.type
        gson.fromJson(InputStreamReader(inputStream), listType)
    } catch (e: FileNotFoundException) {
        emptyList()
    }
}

@Composable
fun AddDataScreen() {
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
        Text(text = "Add Book Details", style = MaterialTheme.typography.headlineMedium)
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
                // Handle adding the data here
                // For demonstration, just print the data
                println("Added book details: Book Name=$bookName, Author Name=$authorName, Price=$price")
                bookName = ""
                authorName = ""
                price = ""
            }
        ) {
            Text("Add")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AddDataScreenPreview() {
//    AddDataScreen()
//}
