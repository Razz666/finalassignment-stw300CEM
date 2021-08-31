package com.rajkumarmagar.locationbasedanythingfinder.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajkumarmagar.locationbasedanythingfinder.R
import com.rajkumarmagar.locationbasedanythingfinder.api.MyServiceBuilder
import com.rajkumarmagar.locationbasedanythingfinder.entity.Post
import com.rajkumarmagar.locationbasedanythingfinder.repository.PostRepository
import com.rajkumarmagar.locationbasedanythingfinder.ui.UpdatePostActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MyPostAdapter(
    val lstPost: MutableList<Post>,
    val context: Context
) : RecyclerView.Adapter<MyPostAdapter.MyPostViewHolder>() {
    class MyPostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCaption1: TextView = view.findViewById(R.id.tvCaption1)
        val tvContact1: TextView = view.findViewById(R.id.tvContact1)
        val imgPost1: ImageView = view.findViewById(R.id.imgPost1)
        val btnUpdate: Button = view.findViewById(R.id.btnUpdate)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.my_post_custom_layout,
            parent,
            false
        )
        return MyPostAdapter.MyPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyPostViewHolder, position: Int) {
        val post = lstPost[position]

        holder.tvCaption1.text = post.postCaption
        ("Contact: " + post.contact).also { holder.tvContact1.text = it }

        val imagePath = MyServiceBuilder.BASE_URL + post.postPhoto
        Glide.with(context)
                .load(imagePath)
                .into(holder.imgPost1)

        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to delete this post?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val postRepository = PostRepository()
                                val response = postRepository.deletePost(post._id!!)
                                if (response.success == true) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show()
                                    }
                                    withContext(Dispatchers.Main) {
                                        lstPost.remove(post)
                                        notifyDataSetChanged()
                                    }
                                }
                            } catch (ex: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                    .setNegativeButton("No") { dialog, id -> //  Action for 'NO' Button
                        dialog.cancel()
                    }

            val alert: AlertDialog = builder.create()
            alert.setTitle("Confirm delete")
            alert.show()
        }

        holder.btnUpdate.setOnClickListener {
            val intent = Intent(context, UpdatePostActivity::class.java)
            intent.putExtra("postId", post._id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return lstPost.size
    }
}