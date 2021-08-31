package com.rajkumarmagar.locationbasedanythingfinder.adapter

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajkumarmagar.locationbasedanythingfinder.R
import com.rajkumarmagar.locationbasedanythingfinder.api.MyServiceBuilder
import com.rajkumarmagar.locationbasedanythingfinder.entity.Post
import com.rajkumarmagar.locationbasedanythingfinder.ui.ShowOnMapActivity

class PostAdapter(
        val lstPost: MutableList<Post>,
        val context: Context
): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvCaption: TextView = view.findViewById(R.id.tvCaption)
        val tvContact: TextView = view.findViewById(R.id.tvContact)
        val imgPost: ImageView = view.findViewById(R.id.imgPost)
        val btnMap: Button = view.findViewById(R.id.btnMap)
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

        holder.btnMap.setOnClickListener {
            val intent = Intent(context.applicationContext, ShowOnMapActivity::class.java)
            intent.putExtra("latitude", post.latitude)
            intent.putExtra("longitude", post.longitude)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.applicationContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return lstPost.size
    }
}