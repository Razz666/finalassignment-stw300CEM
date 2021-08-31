package com.rajkumarmagar.locationbasedanythingfinder.repository

import android.content.Context
import com.rajkumarmagar.locationbasedanythingfinder.api.MyAPIRequest
import com.rajkumarmagar.locationbasedanythingfinder.api.MyServiceBuilder
import com.rajkumarmagar.locationbasedanythingfinder.api.PostAPI
import com.rajkumarmagar.locationbasedanythingfinder.db.PostDB
import com.rajkumarmagar.locationbasedanythingfinder.entity.Post
import com.rajkumarmagar.locationbasedanythingfinder.response.CreateResponse
import com.rajkumarmagar.locationbasedanythingfinder.response.GetPostResponse
import okhttp3.MultipartBody

class PostRepository: MyAPIRequest() {
    private val myAPI = MyServiceBuilder.buildService(PostAPI::class.java)

    suspend fun getAllPost(context: Context): MutableList<Post> {
        val response = apiRequest {
            myAPI.getAllPost(MyServiceBuilder.token!!)
        }
        var user = mutableListOf<Post>()
        if(response.success == true) {
            val allPost: ArrayList<Post> = response.data!!
            PostDB.getInstance(context).clearAllTables()
            PostDB.getInstance(context).getUserDAO().insertPost(allPost)
            user = PostDB.getInstance(context).getUserDAO().getPost()
        }
        return user
    }

    suspend fun getMyPost(context: Context): MutableList<Post> {
        val response = apiRequest {
            myAPI.getMyPost(MyServiceBuilder.token!!)
        }
        var user = mutableListOf<Post>()
        if(response.success == true) {
            val allPost: ArrayList<Post> = response.data!!
            PostDB.getInstance(context).clearAllTables()
            PostDB.getInstance(context).getUserDAO().insertPost(allPost)
            user = PostDB.getInstance(context).getUserDAO().getPost()
        }
        return user
    }

    suspend fun deletePost(_id: String): CreateResponse{
        return apiRequest {
            myAPI.deletePost(MyServiceBuilder.token!!, _id)
        }
    }

    suspend fun createPost(post: Post):CreateResponse {
        return apiRequest {
            myAPI.createPost(MyServiceBuilder.token!!, post)
        }
    }

    suspend fun getPost(_id: String): GetPostResponse {
        return apiRequest {
            myAPI.getPost(MyServiceBuilder.token!!, _id)
        }
    }

    suspend fun uploadPostPhoto(body: MultipartBody.Part, id: String): CreateResponse {
        return apiRequest {
            myAPI.uploadPostPhoto(MyServiceBuilder.token!!, body, id)
        }
    }

    suspend fun updatePost(post: Post): CreateResponse {
        return apiRequest {
            myAPI.updatePost(MyServiceBuilder.token!!, post)
        }
    }
}