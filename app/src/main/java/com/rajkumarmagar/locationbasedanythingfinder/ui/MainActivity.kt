package com.rajkumarmagar.locationbasedanythingfinder.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import com.rajkumarmagar.locationbasedanythingfinder.R

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnLogin: Button
    private lateinit var btnSignup1: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin = findViewById(R.id.btnLogin)
        btnSignup1 = findViewById(R.id.btnSignup1)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        btnLogin.setOnClickListener(this)
        btnSignup1.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            btnLogin -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }

            btnSignup1 -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
        }
    }
}