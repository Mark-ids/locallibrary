package com.qathafiii.locallibrary.screens

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.qathafiii.locallibrary.R
import com.qathafiii.locallibrary.screens.adapters.CategoryAdapter
import com.qathafiii.locallibrary.screens.adapters.ManageCategoryAdapter
import com.qathafiii.locallibrary.screens.models.Category
import com.qathafiii.locallibrary.screens.network.HttpRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference

class ManageActivity : AppCompatActivity(),View.OnClickListener {

    var categories:ArrayList<Category> = ArrayList()
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var manageCategoryAdapter: ManageCategoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)
        progressBar = findViewById(R.id.indeterminateBar)
        recyclerView = findViewById(R.id.rvcategories)

        setSupportActionBar(findViewById(R.id.appbar))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.mipmap.appbarlogo)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)

        findViewById<FloatingActionButton>(R.id.fabadd).setOnClickListener(this)

        getCategories()
    }

    private fun getCategories(){
        val call = callCategories(this)
        call.execute("http://192.168.43.192:4000/category")

    }

    inner class callCategories(context: ManageActivity): AsyncTask<String, String, JSONArray>(), Callback {
        private val activityReference: WeakReference<ManageActivity> = WeakReference(context)
        private var response: JSONArray = JSONArray()
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
                        recyclerView.adapter = ManageCategoryAdapter(categories)
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

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.fabadd -> {
                val context = this
                val dialog = AppCompatDialog(this)
                dialog.setContentView(R.layout.addcategorydialog)
                dialog.setTitle("Add Category")

                val tiTitle = dialog.findViewById<TextInputEditText>(R.id.tititle)
                val tiDescription = dialog.findViewById<TextInputEditText>(R.id.tidescription)
                val tvAdd = dialog.findViewById<TextView>(R.id.tvadd)
                val progressBar = findViewById<ProgressBar>(R.id.indeterminateBar)

                tvAdd!!.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        if( tiTitle!!.text!!.isEmpty()){
                            tiTitle.setHintTextColor(getResources().getColor(R.color.colorInvalid))
                            tiTitle.requestFocus()
                            Toast.makeText(context, "Enter category name", Toast.LENGTH_SHORT).show()
                        }else if( tiDescription!!.text!!.isEmpty()){
                            tiDescription.setHintTextColor(getResources().getColor(R.color.colorInvalid))
                            tiDescription.requestFocus()
                            Toast.makeText(context, "Enter description", Toast.LENGTH_SHORT).show()
                        }else{
                            progressBar.visibility = View.VISIBLE
                            val categoryObj = JSONObject()
                            categoryObj.put("Category_Name",tiTitle.text.toString())
                            categoryObj.put("Description",tiDescription.text.toString())
                            val request = HttpRequest()
                            request.POST("http://192.168.43.192:4000/category",categoryObj.toString(),"application/json; charset=utf-8",null,null,
                                    object : Callback{
                                        override fun onResponse(call: Call?, response: Response?) {
                                            context!!.runOnUiThread({
                                                kotlin.run {
                                                    progressBar.visibility = View.GONE
                                                    Toast.makeText(context, "Successfully added category", Toast.LENGTH_SHORT).show()
                                                    dialog.dismiss()
                                                    getCategories()
                                                }
                                            })
                                        }

                                        override fun onFailure(call: Call?, e: IOException?) {
                                            context!!.runOnUiThread({
                                                kotlin.run {
                                                    progressBar.visibility = View.GONE
                                                    Toast.makeText(context, "Failed to add category", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                        }
                                    })
                            dialog.dismiss()
                        }

                    }
                })

                dialog.show()
            }
        }
    }
}
