package com.qathafiii.locallibrary.screens

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.qathafiii.locallibrary.R
import com.qathafiii.locallibrary.screens.models.Book
import android.content.Intent
import android.net.Uri
import com.google.android.material.floatingactionbutton.FloatingActionButton


class BookActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var book:Book;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        setSupportActionBar(findViewById(R.id.appbar))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.mipmap.appbarlogo)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)

        val book = intent.getSerializableExtra("book") as Book
        this.book = book;

        findViewById<TextView>(R.id.tvtitle).setText(book.title.toUpperCase())
        findViewById<TextView>(R.id.tvauthor).setText(book.author)
        findViewById<TextView>(R.id.tvdescription).setText(book.desc)
        findViewById<FloatingActionButton>(R.id.fabread).setOnClickListener(this)

        val byte = Base64.decode(book.image!!.toByteArray(), Base64.DEFAULT)
        val bookImage = BitmapFactory.decodeByteArray(byte,0,byte.size)
        findViewById<ImageView>(R.id.imgbook).setImageBitmap(bookImage)

        // read


    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.fabread -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(book.pdf))
                startActivity(browserIntent)
            }
        }
    }
}
