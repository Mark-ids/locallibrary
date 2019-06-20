package com.qathafiii.locallibrary.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.qathafiii.locallibrary.R
import com.qathafiii.locallibrary.screens.adapters.ManageCategoryBookAdapter
import com.qathafiii.locallibrary.screens.models.Book
import com.qathafiii.locallibrary.screens.models.Category
import com.qathafiii.locallibrary.screens.network.HttpRequest
import kotlinx.android.synthetic.main.addbookdialog.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.nio.ByteBuffer

class ManageCategoryBooksActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    private lateinit var category:Category

    //Utility Variables
    val REQUEST_GET_SINGLE_FILE = 2
    lateinit var activityDialog: AppCompatDialog
    lateinit var activityImageView: ImageView
    lateinit var imageBase64: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_category_books)

        progressBar = findViewById(R.id.indeterminateBar)
        recyclerView = findViewById(R.id.rvbooks)

        setSupportActionBar(findViewById(R.id.appbar))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.mipmap.appbarlogo)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)

        findViewById<FloatingActionButton>(R.id.fabadd).setOnClickListener(this)

        category = intent.getSerializableExtra("Category") as Category
        getBooks()

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.fabadd -> {
                val context = this
                val dialog = AppCompatDialog(this)
                dialog.setContentView(R.layout.addbookdialog)
                dialog.setTitle("Add Category")

                val image = dialog.findViewById<ImageView>(R.id.imgbook)
                val tiTitle = dialog.findViewById<TextInputEditText>(R.id.tititle)
                val imgBase = dialog.findViewById<TextView>(R.id.img64)
                val tiAuthor = dialog.findViewById<TextInputEditText>(R.id.tiauthor)
                val tiUrl = dialog.findViewById<TextInputEditText>(R.id.tiurl)
                val tiDescription = dialog.findViewById<TextInputEditText>(R.id.tidescription)
                val tvAdd = dialog.findViewById<TextView>(R.id.tvadd)
                val tvAddImage = dialog.findViewById<TextView>(R.id.tvaddimage)
                val progressBar = findViewById<ProgressBar>(R.id.indeterminateBar)

                tvAddImage!!.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        dialog.dismiss()
                        activityDialog = dialog
                        activityImageView = image!!
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.type = "image/*"
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_FILE)
                    }
                })

                tvAdd!!.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        //imgBase?.setText(imageBase64)
                        if( tiTitle!!.text!!.isEmpty()){
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
                                    object : Callback{
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
                        }

                    }
                })

                dialog.show()
            }
        }
    }

    fun getBooks(){
        val call = callBooks(this)
        val url = "http://192.168.43.192:4000/books?category_id=" + category.categoryId
        call.execute(url)
    }

    inner class callBooks(context: ManageCategoryBooksActivity): AsyncTask<String, String, JSONArray>(), Callback {
        private val activityReference: WeakReference<ManageCategoryBooksActivity> = WeakReference(context)
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
                        recyclerView.adapter = ManageCategoryBookAdapter(books)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_GET_SINGLE_FILE -> {
                    val pickedImage: Uri? = data?.getData()
        // Let's read picked image path using content resolver
                    val inputStream = contentResolver.openInputStream(pickedImage)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                            val byteArray = byteArrayOutputStream .toByteArray()
                    val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    imageBase64 = encoded
                    /*val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(pickedImage, filePath, null, null, null)
                    cursor.moveToFirst()
                    val imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]))

                    val options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888
                    cursor.close()
                  */

         // Do something with the bitmap
                    activityImageView.setImageBitmap(bitmap)
                    activityDialog.show()


        // At the end remember to close the cursor or you will end with the RuntimeException!
                    inputStream.close()
                }
            }
        }else{

        }

    }
}
