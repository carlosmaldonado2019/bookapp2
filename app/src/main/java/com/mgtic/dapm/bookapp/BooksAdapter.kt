package com.mgtic.dapm.bookapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class BooksAdapter: RecyclerView.Adapter<BooksAdapter.ViewHolder>(){
    var books: List<book> = ArrayList()
    lateinit var context: Context

    fun BooksAdapter(books:ArrayList<book>,context: Context){
        this.books = books
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.items_book,parent,false))
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = books.get(position)
        holder.bind(item,context)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tiitle = view.findViewById<TextView>(R.id.tvTittle)
        val author = view.findViewById<TextView>(R.id.tvAuthor)
        val publisher = view.findViewById<TextView>(R.id.tvPublisher)
        val imagen = view.findViewById<ImageView>(R.id.ivImagen)
        fun bind(book:book,context: Context){
            tiitle.text = book.title
            author.text = book.author
            publisher.text = book.publisher
            imagen.loadUrl(book.imageUrl)
        }
        fun ImageView.loadUrl(url: String) {
            Picasso.with(context).load(url).into(this)
            //Picasso.with(context).load(url).placeholder(R.drawable.ic_launcher_background).into(this)
        }
    }



}