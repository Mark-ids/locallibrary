package com.qathafiii.locallibrary.screens.adapters

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.qathafiii.locallibrary.R
import com.qathafiii.locallibrary.screens.ManageCategoryBooksActivity
import com.qathafiii.locallibrary.screens.models.Category

class ManageCategoryAdapter(val categories:ArrayList<Category>): RecyclerView.Adapter<ManageCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView),View.OnClickListener{
        var pos = -1
        var tvCategoryTitle:TextView
        var tvCategoryDescription:TextView
        var tvUpdate:TextView
        var tvDelete: TextView
        init {
            tvCategoryTitle = itemView.findViewById(R.id.tvcategorytitle)
            tvCategoryDescription = itemView.findViewById(R.id.tvcategorydescription)
            tvUpdate = itemView.findViewById(R.id.tvupdate)
            tvDelete = itemView.findViewById(R.id.tvdelete)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val intent = Intent(p0?.context,ManageCategoryBooksActivity::class.java)
            intent.putExtra("Category",categories[pos])
            p0?.context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageCategoryAdapter.ViewHolder {
        val inflater:LayoutInflater = LayoutInflater.from(parent.getContext())
        val categoryView = inflater.inflate(R.layout.rowmanageactivity, parent, false)

        // Return a new holder instance
        return ViewHolder(categoryView)
    }

    override fun onBindViewHolder(viewHolder: ManageCategoryAdapter.ViewHolder, pos: Int) {
        viewHolder.pos = pos
        val category = categories.get(pos)
        val tvCategoryTitle1 = viewHolder.tvCategoryTitle
        val tvCategoryDescription = viewHolder.tvCategoryDescription
        val tvUpdate = viewHolder.tvUpdate
        val tvDelete = viewHolder.tvDelete

        tvCategoryTitle1.setText(category.categoryName)
        tvCategoryDescription.setText(category.categoryDescription)

        tvUpdate.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        tvDelete.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}