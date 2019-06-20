package com.qathafiii.locallibrary.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.qathafiii.locallibrary.R
import com.qathafiii.locallibrary.screens.viewmodels.UserProfileViewModel

class RegisterActivity : AppCompatActivity(),TextWatcher,Observer<String> , View.OnClickListener{
    private val viewModel:UserProfileViewModel by lazy {
        ViewModelProviders.of(this).get(UserProfileViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(findViewById(R.id.appbar))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.mipmap.appbarlogo)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)

        init()

        viewModel.response.observe(this, this)
    }

    private fun init(){
        findViewById<TextInputEditText>(R.id.tvfirstname).addTextChangedListener(this)
        findViewById<TextInputEditText>(R.id.tvlastname).addTextChangedListener(this)
        findViewById<TextInputEditText>(R.id.tvusername).addTextChangedListener(this)
        findViewById<TextInputEditText>(R.id.tvpassword).addTextChangedListener(this)

        /** fill the ui*/
        findViewById<TextInputEditText>(R.id.tvfirstname).setText(viewModel.user.First_Name)
        findViewById<TextInputEditText>(R.id.tvlastname).setText(viewModel.user.Last_Name)
        findViewById<TextInputEditText>(R.id.tvusername).setText(viewModel.user.Username)
        findViewById<TextInputEditText>(R.id.tvpassword).setText(viewModel.user.Password)

        findViewById<Button>(R.id.btnregister).setOnClickListener(this)
    }


    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        viewModel.user.First_Name = findViewById<TextInputEditText>(R.id.tvfirstname).text.toString()
        viewModel.user.Last_Name = findViewById<TextInputEditText>(R.id.tvlastname).text.toString()
        viewModel.user.Username = findViewById<TextInputEditText>(R.id.tvusername).text.toString()
        viewModel.user.Password = findViewById<TextInputEditText>(R.id.tvpassword).text.toString()
    }

    override fun onChanged(t: String?) {
        Toast.makeText(this, t, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnregister -> {
                viewModel.registerUser()
            }
        }
    }

}
