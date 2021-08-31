package com.rajkumarmagar.locationbasedanythingfinder.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rajkumarmagar.locationbasedanythingfinder.R
import com.rajkumarmagar.locationbasedanythingfinder.api.MyServiceBuilder


class ButtomNavDashboardActivity : AppCompatActivity(), SensorEventListener{
    private lateinit var sensorManager: SensorManager
    private var sensorProximity: Sensor? = null
    private var sensorLight: Sensor? = null
    private var sensorGyro: Sensor? = null

    private val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buttom_nav_dashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_create_Post, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (!hasPermission()) {
            requestPermission()
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if(!checkSensor())
            return
        else
            sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL)

            sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL)

            sensorGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, sensorGyro, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
                this,
                permissions,
                1)
    }

    private fun hasPermission(): Boolean{
        var hasPermission = true
        for(permission in permissions){
            if(ActivityCompat.checkSelfPermission(
                            this,
                            permission
                    )!= PackageManager.PERMISSION_GRANTED
            ){
                hasPermission = false
            }
        }
        return hasPermission
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_PROXIMITY) {
            val values = event.values[0]

            if(values <= 4)
                confirmLogout()
        }

        else if(event.sensor.type == Sensor.TYPE_LIGHT) {
            val values = event.values[0]

            if(values > 450) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            }
        }

        else if(event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            val values = event.values[1]

            if(values < 0) {
                Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show()
            }
            else if(values > 0) {
                Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun checkSensor(): Boolean {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null
            && sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null
            && sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
            flag = false
        }
        return flag
    }

    private fun confirmLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                logout()
            }
            .setNegativeButton("No") { dialog, id -> //  Action for 'NO' Button
                dialog.cancel()
            }

        val alert: AlertDialog = builder.create()
        alert.setTitle("Confirm logout")
        alert.show()
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("LoginCredentials", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        MyServiceBuilder.token = ""
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}