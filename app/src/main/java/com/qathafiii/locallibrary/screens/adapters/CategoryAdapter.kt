package com.qathafiii.locallibrary.screens.adapters

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.qathafiii.locallibrary.R
import com.qathafiii.locallibrary.screens.CategoryBooksActivity
import com.qathafiii.locallibrary.screens.models.Category

class CategoryAdapter(val categories:ArrayList<Category>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView),View.OnClickListener{
        var pos = -1
        var tvCategoryTitle:TextView
        var tvCategoryDescription:TextView
        init {
            tvCategoryTitle = itemView.findViewById(R.id.tvcategorytitle)
            tvCategoryDescription = itemView.findViewById(R.id.tvcategorydescription)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val intent = Intent(p0?.context, CategoryBooksActivity::class.java)
            intent.putExtra("Category",categories[pos])
            p0?.context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val inflater:LayoutInflater = LayoutInflater.from(parent.getContext())
        val categoryView = inflater.inflate(R.layout.rowcategoriesactivity, parent, false)

        // Return a new holder instance
        return ViewHolder(categoryView)
    }

    override fun onBindViewHolder(viewHolder: CategoryAdapter.ViewHolder, pos: Int) {
        viewHolder.pos = pos
        val category = categories.get(pos)
        val tvCategoryTitle1 = viewHolder.tvCategoryTitle
        val tvCategoryDescription = viewHolder.tvCategoryDescription

        tvCategoryTitle1.setText(category.categoryName)
        tvCategoryDescription.setText(category.categoryDescription)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}