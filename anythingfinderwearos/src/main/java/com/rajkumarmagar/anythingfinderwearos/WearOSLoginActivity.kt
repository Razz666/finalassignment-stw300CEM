package com.rajkumarmagar.anythingfinderwearos

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.rajkumarmagar.anythingfinderwearos.api.MyServiceBuilder
import com.rajkumarmagar.anythingfinderwearos.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class WearOSLoginActivity : WearableActivity() {
    private lateinit var linearLayout: LinearLayout
    private lateinit var etUsername1: EditText
    private lateinit var etPassword1: EditText
    private lateinit var btnLogin1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wear_o_s_login)

        // Enables Always-on
        setAmbientEnabled()

        linearLayout = findViewById(R.id.linearLayout)
        etUsername1 = findViewById(R.id.etUsername1)
        etPassword1 = findViewById(R.id.etPassword1)
        btnLogin1 = findViewById(R.id.btnLogin1)

        btnLogin1.setOnClickListener {
            login()
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

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@WearOSLoginActivity, "Login success", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this@WearOSLoginActivity, DashboardActivity::class.java))
                    }
                } else {
                    withContext(Dispatchers.Main) {
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
                withContext(Dispatchers.Main) {
                    withContext(Dispatchers.Main) {
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
}