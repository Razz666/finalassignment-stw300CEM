package com.rajkumarmagar.locationbasedanythingfinder.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.rajkumarmagar.locationbasedanythingfinder.R
//import com.rajkumarmagar.locationbasedanythingfinder.db.UserDB
import com.rajkumarmagar.locationbasedanythingfinder.entity.User
import com.rajkumarmagar.locationbasedanythingfinder.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var tvHaveAccount: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnSignup = findViewById(R.id.btnSignup)
        tvHaveAccount = findViewById(R.id.tvHaveAccount)

        btnSignup.setOnClickListener {
            val email = etEmail.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            val user = User(username = username, email = email, password = password)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepository = UserRepository()
                    val response = userRepository.registerUser(user)
                    if (response.success == true) {
                        withContext(Main) {
                            Toast.makeText(
                                    this@SignUpActivity,
                                    "User Registered",
                                    Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Main) {
                        Toast.makeText(this@SignUpActivity, ex.toString(), Toast.LENGTH_SHORT)
                                .show()
                    }
                }
            }
        }

        tvHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}