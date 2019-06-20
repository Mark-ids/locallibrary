package com.qathafiii.locallibrary.screens.adapters

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.qathafiii.locallibrary.R
import com.qathafiii.locallibrary.screens.BookActivity
import com.qathafiii.locallibrary.screens.models.Book

class CategoryBooksAdapter(val books:ArrayList<Book>): RecyclerView.Adapter<CategoryBooksAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var pos = -1
        var tvTitle: TextView
        var tvAuthor: TextView
        init {
            tvTitle = itemView.findViewById(R.id.tvtitle)
            tvAuthor = itemView.findViewById(R.id.tvauthor)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val intent = Intent(p0?.context, BookActivity::class.java)
            intent.putExtra("book",books[pos])
            p0?.context?.startActivity(intent)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryBooksAdapter.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.getContext())
        val categoryView = inflater.inflate(R.layout.rowmanagecategorybook, parent, false)

        // Return a new holder instance
        return ViewHolder(categoryView)
    }

    override fun onBindViewHolder(viewHolder: CategoryBooksAdapter.ViewHolder, pos: Int) {
        viewHolder.pos = pos
        val book = books.get(pos)
        val tvCategoryTitle1 = viewHolder.tvTitle
        val tvAuthor = viewHolder.tvAuthor

        tvCategoryTitle1.setText(book.title.toUpperCase())
        tvAuthor.setText(book.author)

    }

    override fun getItemCount(): Int {
        return books.size
    }
}