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
import com.qathafiii.locallibrary.screens.adapters.CategoryAdapter
import com.qathafiii.locallibrary.screens.models.Category
import com.qathafiii.locallibrary.screens.network.HttpRequest
import org.json.JSONArray
import java.lang.ref.WeakReference
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.util.Log

class CategoriesActivity : AppCompatActivity() {

    var categories:ArrayList<Category> = ArrayList()
    lateinit var progressBar:ProgressBar
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        progressBar = findViewById(R.id.indeterminateBar)
        recyclerView = findViewById(R.id.rvcategories)

        setSupportActionBar(findViewById(R.id.appbar))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.mipmap.appbarlogo)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)

        getCategories()
    }

    private fun getCategories(){
        val call = callCategories(this)
        call.execute("http://192.168.43.192:4000/category")

    }

    inner class callCategories(context: CategoriesActivity): AsyncTask<String,String,JSONArray>(),Callback{
        private val activityReference: WeakReference<CategoriesActivity> = WeakReference(context)
        private var response:JSONArray = JSONArray()
       override fun onPreExecute(){
           val activity = activityReference.get()
           if (activity == null || activity.isFinishing) return
           activity.progressBar.visibility = View.VISIBLE
       }

        override fun doInBackground(vararg p0: String): JSONArray {
            publishProgress("Fetching Book Categories...")
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

            val text = "No categories"
            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return
            activity.progressBar.visibility = View.GONE



        }

        override fun onResponse(call: Call?, response: Response?) {
            //Log.e("response",response?.body()?.string())
            val text = "No categories"
            val activity = activityReference.get()
            val jsonResponse = JSONArray(response?.body()?.string() )
            if(jsonResponse.length() != 0){
                this.response = jsonResponse
                for(index in 0 until this.response.length()){
                    val categoryJson = this.response.getJSONObject(index)
                    val bookCategory = Category(categoryJson.getInt("Category_Identifier"),categoryJson.getString("Category_Name"),categoryJson.getString("Description"))
                    categories.add(bookCategory)
                }
                activity!!.runOnUiThread({
                    kotlin.run {
                        recyclerView.adapter = CategoryAdapter(categories)
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
