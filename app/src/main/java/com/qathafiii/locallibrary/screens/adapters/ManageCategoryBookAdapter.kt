package com.qathafiii.locallibrary.screens.adapters

import android.content.Intent
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatDialog
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.qathafiii.locallibrary.R
import com.qathafiii.locallibrary.screens.BookActivity
import com.qathafiii.locallibrary.screens.models.Book
import com.qathafiii.locallibrary.screens.network.HttpRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ManageCategoryBookAdapter(val books:ArrayList<Book>): RecyclerView.Adapter<ManageCategoryBookAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var pos = -1
        var tvTitle: TextView
        var tvAuthor: TextView
        var tvUpdate: TextView
        var tvDelete: TextView
        init {
            tvTitle = itemView.findViewById(R.id.tvtitle)
            tvAuthor = itemView.findViewById(R.id.tvauthor)
            tvUpdate = itemView.findViewById(R.id.tvupdate)
            tvDelete = itemView.findViewById(R.id.tvdelete)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val intent = Intent(p0?.context, BookActivity::class.java)
            intent.putExtra("book",books[pos])
            p0?.context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageCategoryBookAdapter.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.getContext())
        val categoryView = inflater.inflate(R.layout.rowmanagecategorybook, parent, false)

        // Return a new holder instance
        return ViewHolder(categoryView)
    }

    override fun onBindViewHolder(viewHolder: ManageCategoryBookAdapter.ViewHolder, pos: Int) {
        viewHolder.pos = pos
        val book = books.get(pos)
        val tvCategoryTitle1 = viewHolder.tvTitle
        val tvAuthor = viewHolder.tvAuthor
        val tvUpdate = viewHolder.tvUpdate
        val tvDelete = viewHolder.tvDelete

        tvCategoryTitle1.setText(book.title.toUpperCase())
        tvAuthor.setText(book.author)

        tvUpdate.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val context = p0?.context
                val dialog = AppCompatDialog(p0?.context)
                dialog.setContentView(R.layout.addbookdialog)
                dialog.setTitle("Add Category")

                val image = dialog.findViewById<ImageView>(R.id.imgbook)
                val tiTitle = dialog.findViewById<TextInputEditText>(R.id.tititle)
                val tiAuthor = dialog.findViewById<TextInputEditText>(R.id.tiauthor)
                val tiUrl = dialog.findViewById<TextInputEditText>(R.id.tiurl)
                val tiDescription = dialog.findViewById<TextInputEditText>(R.id.tidescription)
                val tvAdd = dialog.findViewById<TextView>(R.id.tvadd)
                val tvAddImage = dialog.findViewById<TextView>(R.id.tvaddimage)

                tvAddImage!!.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        dialog.dismiss()
                        //activityDialog = dialog
                        //activityImageView = image!!
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.type = "image/*"
                        //startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_FILE)
                    }
                })

                tvAdd!!.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        /*if( tiTitle!!.text!!.isEmpty()){
                            tiTitle.setHintTextColor(getResources().getColor(R.color.colorInvalid))
                            tiTitle.requestFocus()
                            Toast.makeText(context, "Enter category name", Toast.LENGTH_SHORT).show()
                        }else if( tiAuthor!!.text!!.isEmpty()){
                            tiAuthor.setHintTextColor(getResources().getColor(R.color.colorInvalid))
                            tiAuthor.requestFocus()
                            Toast.makeText(context, "Enter description", Toast.LENGTH_SHORT).show()
                        }else if( tiDescription!!.text!!.isEmpty()){
                            tiDescription.setHintTextColor(getResources().getColor(R.color.colorInvalid))
                            tiDescription.requestFocus()
                            Toast.makeText(context, "Enter description", Toast.LENGTH_SHORT).show()
                        }else{
                            progressBar.visibility = View.VISIBLE

                            val bookObj = JSONObject()
                            bookObj.put("Category_Identifier",category.categoryId)
                            bookObj.put("Book_Name",tiTitle.text.toString())
                            bookObj.put("Author",tiAuthor.text.toString())
                            bookObj.put("Book_Url",tiUrl!!.text.toString())
                            bookObj.put("Description",tiDescription.text.toString())
                            bookObj.put("Book_Image",imageBase64)
                            val request = HttpRequest()
                            request.POST("http://192.168.43.192:4000/books",bookObj.toString(),"application/json; charset=utf-8",null,null,
                                    object : Callback {
                                        override fun onResponse(call: Call?, response: Response?) {
                                            context!!.runOnUiThread({
                                                kotlin.run {
                                                    progressBar.visibility = View.GONE
                                                    Toast.makeText(context, "Successfully added book", Toast.LENGTH_SHORT).show()
                                                    dialog.dismiss()
                                                    getBooks()
                                                }
                                            })
                                        }

                                        override fun onFailure(call: Call?, e: IOException?) {
                                            context!!.runOnUiThread({
                                                kotlin.run {
                                                    progressBar.visibility = View.GONE
                                                    Toast.makeText(context, "Failed to add book", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                        }
                                    })
                            dialog.dismiss()
                        }*/

                    }
                })

                dialog.show()
            }
        })

        tvDelete.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun getItemCount(): Int {
        return books.size
    }


}