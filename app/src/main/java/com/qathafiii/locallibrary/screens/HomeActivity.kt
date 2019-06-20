package com.qathafiii.locallibrary.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.qathafiii.locallibrary.R

class HomeActivity : AppCompatActivity() {
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(findViewById(R.id.appbar))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.mipmap.appbarlogo)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)

        findViewById<FloatingActionButton>(R.id.fabread).setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?){
                val intent = Intent(context, CategoriesActivity::class.java)
                startActivity(intent)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_login -> {
            val intent = Intent(this, SignInActivity::class.java)
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
        getMenuInflater().inflate(R.menu.appmenu,menu)
        return true
    }
}
