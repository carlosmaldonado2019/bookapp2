package com.mgtic.dapm.bookapp

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
     val bookFetchUrl = "https://www.googleapis.com/books/v1/volumes"
     lateinit var mRecyclerView: RecyclerView
     val mAdapter: BooksAdapter = BooksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddTask.setOnClickListener {
            var etTask = findViewById<TextView>(R.id.etTask)
            var query:String = etTask.text.toString()
            val baseUri = Uri.parse(bookFetchUrl)
            val uriBuilder = baseUri.buildUpon()

            uriBuilder.appendQueryParameter("q", query)
            getBooks(uriBuilder)
        }
    }
    fun getBooks(uri:Uri.Builder) {
        val dialog = progressDialog(message = "Please wait a bit…", title = "Fetching data")
        dialog.show()
        doAsync {
            val result = URL(uri.toString()).readText()
         //   tasks = MisNotasApp.database.taskDao().getAllTasks()
            uiThread {
                var title = "title"
                var author = "author"
                var infoUrl = "infoUrl"
                var imageUrl = "imageUrl"
                var publisher = "publisher"

                val bookList = ArrayList<book>()
                val jsonObject = JSONObject(result)
                val items = jsonObject.getJSONArray("items")

                for (i in 0 until items.length()) {
                    val jsonObject1 = items.getJSONObject(i)
                    val volumeInfo = jsonObject1.getJSONObject("volumeInfo")
                    title = volumeInfo.getString("title")
                    if (volumeInfo.has("publisher")) {
                        publisher = volumeInfo.getString("publisher")
                    }

                    if (volumeInfo.has("authors")) {

                        val authors = volumeInfo.getJSONArray("authors")
                        author = authors.getString(0)
                    }
                    infoUrl = volumeInfo.getString("infoLink")
                    if (volumeInfo.has("imageLinks")) {
                        val imageLinks = volumeInfo.getJSONObject("imageLinks")
                        imageUrl = imageLinks.getString("smallThumbnail")
                    }
                    val bookItem = book(title, author, infoUrl, imageUrl, publisher)
                    bookList.add(bookItem)
                }

                setUpRecyclerView(bookList)
                dialog.cancel()
            }
        }
    }

    fun setUpRecyclerView(bookList: ArrayList<book>) {
        mRecyclerView = findViewById(R.id.rvTask)as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.BooksAdapter(bookList,this)
        mRecyclerView.adapter = mAdapter
    }


    fun clearFocus(){
        etTask.setText("")
    }

    fun Context.hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}
