package com.rajkumarmagar.locationbasedanythingfinder.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.rajkumarmagar.locationbasedanythingfinder.R
import com.rajkumarmagar.locationbasedanythingfinder.api.MyServiceBuilder
//import com.rajkumarmagar.locationbasedanythingfinder.db.UserDB
import com.rajkumarmagar.locationbasedanythingfinder.entity.User
import com.rajkumarmagar.locationbasedanythingfinder.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var linearLayout: LinearLayout
    private lateinit var etUsername1: EditText
    private lateinit var etPassword1: EditText
    private lateinit var btnLogin1: Button
    private lateinit var tvNoAccount: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        linearLayout = findViewById(R.id.linearLayout)
        etUsername1 = findViewById(R.id.etUsername1)
        etPassword1 = findViewById(R.id.etPassword1)
        btnLogin1 = findViewById(R.id.btnLogin1)
        tvNoAccount = findViewById(R.id.tvNoAccount)

        btnLogin1.setOnClickListener(this)
        tvNoAccount.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            btnLogin1 -> {
                if (checkEmptyValues()) {
                    login()
                }
            }

            tvNoAccount -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
        }
    }

    private fun login() {
        val username = etUsername1.text.toString()
        val password = etPassword1.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.login(username, password)

                if (response.success == true) {
                    MyServiceBuilder.token = "Bearer ${response.token}"

                    val user: User? = response.clientData
                    if (user != null) {
                        saveLoginCredentialsSharedPreferences(user, password)
                    }

                    withContext(Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login success",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this@LoginActivity, ButtomNavDashboardActivity::class.java))
                        finish()
                    }
                } else {
                    withContext(Main) {
                        val snack =
                            Snackbar.make(
                                linearLayout,
                                "Invalid credentials",
                                Snackbar.LENGTH_LONG
                            )
                        snack.setAction("OK") {
                            snack.dismiss()
                        }
                        snack.show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Main) {
                    withContext(Main) {
                        val snack =
                            Snackbar.make(
                                linearLayout,
                                "Invalid credentials",
                                Snackbar.LENGTH_LONG
                            )
                        snack.setAction("OK") {
                            snack.dismiss()
                        }
                        snack.show()
                    }
                }
            }
        }
    }

    private fun checkEmptyValues(): Boolean {
        var flag = true
        when {
            TextUtils.isEmpty(etUsername1.text) -> {
                etUsername1.error = "Enter your email or username"
                etUsername1.requestFocus()
                flag = false
            }
            TextUtils.isEmpty(etPassword1.text) -> {
                etPassword1.error = "Enter your password"
                etPassword1.requestFocus()
                flag = false
            }
        }
        return flag
    }

    private fun saveLoginCredentialsSharedPreferences(user: User, password: String) {
        val sharedPreferences = getSharedPreferences("LoginCredentials", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", user.username)
        editor.putString("password", password)
        editor.putString("email", user.email)
        editor.apply()
    }
}