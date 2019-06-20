package com.qathafiii.locallibrary.screens

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.qathafiii.locallibrary.R
import com.qathafiii.locallibrary.screens.network.HttpRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference

class SignInActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var progressBar: ProgressBar
    lateinit var tvUserName: TextInputEditText
    lateinit var tvPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        progressBar = findViewById(R.id.indeterminateBar)
        tvUserName = findViewById(R.id.tvusername)
        tvPassword = findViewById(R.id.tvpassword)

        setSupportActionBar(findViewById(R.id.appbar))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.mipmap.appbarlogo)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)

        findViewById<Button>(R.id.btnlogin).setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_register -> {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        getMenuInflater().inflate(R.menu.loginappmenu,menu)
        return true
    }

    inner class callLogin(context: SignInActivity): AsyncTask<String, String, JSONObject>(), Callback {
        private val activityReference: WeakReference<SignInActivity> = WeakReference(context)
        private var response = JSONObject()
        override fun onPreExecute(){
            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return
            activity.progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg p0: String): JSONObject {
            publishProgress("Loggin in...")
            val request = HttpRequest()
            request.POST(p0[0],p0[1],"application/json; charset=utf-8",null,null,this)
            return response
        }

        override fun onProgressUpdate(vararg text: String?) {

            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return

            Toast.makeText(activity, text.firstOrNull(), Toast.LENGTH_SHORT).show()

        }

        override fun onPostExecute(result: JSONObject) {
            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return
            activity.progressBar.visibility = View.GONE


        }

        override fun onResponse(call: Call?, response: Response?) {
            //Log.e("response",response?.body()?.string())
            val text = "Wrong username or password"
            val activity = activityReference.get()
            val jsonResponse = JSONObject(response?.body()?.string() )
            if(jsonResponse != null){
                this.response = jsonResponse
                if(this.response.getString("message").toString().equals("Invalid username or password",true)){
                    activity!!.runOnUiThread({
                        kotlin.run {
                            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
                        }
                    })

                }else if(this.response.getString("message").toString().equals("login successful",true)){
                    activity!!.runOnUiThread({
                        kotlin.run {
                            Toast.makeText(activity, "Successful login", Toast.LENGTH_SHORT).show()
                            val intent = Intent(activity, ManageActivity::class.java)
                            startActivity(intent)
                        }
                    })

                }
            }else {
                Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show()
            }
            response?.close()
        }

        override fun onFailure(call: Call?, e: IOException?) {
            val activity = activityReference.get()
            Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnlogin -> {
                if(validateForm()){
                    val body = JSONObject()
                    body.put("Username",tvUserName.text!!.toString())
                    body.put("Password",tvPassword.text!!.toString())
                    val call = callLogin(this)
                    call.execute("http://192.168.43.192:4000/login",body.toString())
                }else{
                    return
                }
            }
        }
    }

    private fun validateForm(): Boolean{
        var validForm = true
        if( tvUserName.text!!.isEmpty()){
            tvUserName.setHintTextColor(getResources().getColor(R.color.colorInvalid))
            tvUserName.requestFocus()
            Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show()
            validForm = false
        }else if( tvPassword.text!!.isEmpty()){
            tvPassword.setHintTextColor(getResources().getColor(R.color.colorInvalid))
            tvPassword.requestFocus()
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
            validForm = false
        }

        return validForm
    }
}
