package com.rajkumarmagar.anythingfinderwearos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rajkumarmagar.anythingfinderwearos.adapter.PostAdapter
import com.rajkumarmagar.anythingfinderwearos.repository.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        recyclerView = findViewById(R.id.recyclerView)

        CoroutineScope(Dispatchers.IO).launch {
            val repository = PostRepository()
            val response = repository.getAllPost()

            withContext(Dispatchers.Main) {
                val adapter = PostAdapter(response, this@DashboardActivity)
                recyclerView.layoutManager = LinearLayoutManager(this@DashboardActivity)
                recyclerView.adapter = adapter
            }
        }
    }
}