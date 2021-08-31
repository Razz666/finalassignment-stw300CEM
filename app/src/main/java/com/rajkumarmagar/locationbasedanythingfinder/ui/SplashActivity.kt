package com.rajkumarmagar.locationbasedanythingfinder.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.rajkumarmagar.locationbasedanythingfinder.R
import com.rajkumarmagar.locationbasedanythingfinder.api.MyServiceBuilder
import com.rajkumarmagar.locationbasedanythingfinder.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.io.IOException

class SplashActivity : AppCompatActivity() {
    private lateinit var relativeLayout: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        relativeLayout = findViewById(R.id.relativeLayout)

        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences("LoginCredentials", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        val password = sharedPreferences.getString("password", null)

        login(username, password)
    }

    private fun login(username: String?, password: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (username == null && password == null) {
                withContext(Main) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }

            else {
                try {
                    val repository = UserRepository()
                    val response = repository.login(username.toString(), password.toString())

                    if (response.success == true) {
                        MyServiceBuilder.token = "Bearer ${response.token}"

                        startActivity(Intent(this@SplashActivity, ButtomNavDashboardActivity::class.java))
                        finish()
                    }
                }
                catch (ex: IOException) {
                    withContext(Main) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}