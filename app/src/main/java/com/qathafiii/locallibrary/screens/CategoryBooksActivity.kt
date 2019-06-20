package com.qathafiii.locallibrary.screens

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.qathafiii.locallibrary.R
import com.qathafiii.locallibrary.screens.adapters.CategoryBooksAdapter
import com.qathafiii.locallibrary.screens.models.Book
import com.qathafiii.locallibrary.screens.models.Category
import com.qathafiii.locallibrary.screens.network.HttpRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.lang.ref.WeakReference

class CategoryBooksActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    private lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_books)
        progressBar = findViewById(R.id.indeterminateBar)
        recyclerView = findViewById(R.id.rvbooks)

        setSupportActionBar(findViewById(R.id.appbar))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.mipmap.appbarlogo)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)

        category = intent.getSerializableExtra("Category") as Category
        getBooks()
    }

    fun getBooks(){
        val call = callBooks(this)
        val url = "http://192.168.43.192:4000/books?category_id=" + category.categoryId
        call.execute(url)
    }

    inner class callBooks(context: CategoryBooksActivity): AsyncTask<String, String, JSONArray>(), Callback {
        private val activityReference: WeakReference<CategoryBooksActivity> = WeakReference(context)
        private var response: JSONArray = JSONArray()
        override fun onPreExecute(){
            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return
            activity.progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg p0: String): JSONArray {
            publishProgress("Fetching Books...")
            val request = HttpRequest()
            request.GET(p0[0],null,null,this)
            return response
        }

        override fun onProgressUpdate(vararg text: String?) {

            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return

            Toast.makeText(activity, text.firstOrNull(), Toast.LENGTH_SHORT).show()

        }

        override fun onPostExecute(result: JSONArray) {
            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return
            activity.progressBar.visibility = View.GONE
        }

        override fun onResponse(call: Call?, response: Response?) {
            //Log.e("response",response?.body()?.string())
            val text = "No Books"
            val activity = activityReference.get()
            val jsonResponse = JSONArray(response?.body()?.string() )
            if(jsonResponse.length() != 0){
                val books = ArrayList<Book>()
                this.response = jsonResponse
                for(index in 0 until this.response.length()){
                    val bookJson = this.response.getJSONObject(index)
                    val book = Book(bookJson.getInt("Book_Identifier"),bookJson.getInt("Category_Identifier"), bookJson.getString("Book_Name"),
                            bookJson.getString("Book_Name"),bookJson.optString("Description"),bookJson.optString("Book_Url"),
                            bookJson.optString("Book_Image"))
                    books.add(book)
                }

                activity!!.runOnUiThread({
                    kotlin.run {
                        recyclerView.adapter = CategoryBooksAdapter(books)
                        recyclerView.layoutManager = LinearLayoutManager(activity)
                    }
                })
            }else {
                activity!!.runOnUiThread({
                    kotlin.run {
                        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            response?.close()
        }

        override fun onFailure(call: Call?, e: IOException?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}
