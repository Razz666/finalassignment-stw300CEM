package com.rajkumarmagar.locationbasedanythingfinder.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.rajkumarmagar.locationbasedanythingfinder.R
import com.rajkumarmagar.locationbasedanythingfinder.entity.Post
import com.rajkumarmagar.locationbasedanythingfinder.repository.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdatePostActivity : AppCompatActivity() {
    private lateinit var etUpdateCaption: EditText
    private lateinit var etUpdateContact: EditText
    private lateinit var btnUpdatePost: Button
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_post)

        etUpdateCaption = findViewById(R.id.etUpdateCaption)
        etUpdateContact = findViewById(R.id.etUpdateContact)
        btnUpdatePost = findViewById(R.id.btnUpdatePost)

        title = "Update Post"

        val postId = intent.getStringExtra("postId")

        getPost(postId!!)

        btnUpdatePost.setOnClickListener {
            updatePost()
        }
    }

    private fun getPost(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = PostRepository()
                val response = repository.getPost(id)

                if (response.success == true) {
                    post = response.post!!
                    etUpdateCaption.setText(post.postCaption)
                    etUpdateContact.setText(post.contact)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdatePostActivity, e.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun updatePost() {
        val caption = etUpdateCaption.text.toString()
        val contact = etUpdateContact.text.toString()

        val post = Post(_id = post._id, postCaption = caption, contact = contact)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = PostRepository()
                val response = repository.updatePost(post)

                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@UpdatePostActivity, response.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@UpdatePostActivity, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UpdatePostActivity, e.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    }
}