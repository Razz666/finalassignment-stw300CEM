package com.rajkumarmagar.anythingfinderwearos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajkumarmagar.anythingfinderwearos.R
import com.rajkumarmagar.anythingfinderwearos.api.MyServiceBuilder
import com.rajkumarmagar.anythingfinderwearos.entity.Post

class PostAdapter(
    val lstPost: MutableList<Post>,
    val context: Context
): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvCaption: TextView = view.findViewById(R.id.tvCaption)
        val tvContact: TextView = view.findViewById(R.id.tvContact)
        val imgPost: ImageView = view.findViewById(R.id.imgPost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_custom_layout, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = lstPost[position]

        ("posted by "+post.username).also { holder.tvUsername.text = it }
        holder.tvCaption.text = post.postCaption
        ("Contact: "+post.contact).also { holder.tvContact.text = it }

        val imagePath = MyServiceBuilder.BASE_URL + post.postPhoto
        Glide.with(context)
                .load(imagePath)
                .into(holder.imgPost)
    }

    override fun getItemCount(): Int {
       return lstPost.size
    }
}