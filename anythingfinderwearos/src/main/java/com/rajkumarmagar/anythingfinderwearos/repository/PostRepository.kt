package com.rajkumarmagar.anythingfinderwearos.repository

import com.rajkumarmagar.anythingfinderwearos.api.MyAPIRequest
import com.rajkumarmagar.anythingfinderwearos.api.MyServiceBuilder
import com.rajkumarmagar.anythingfinderwearos.api.PostAPI
import com.rajkumarmagar.anythingfinderwearos.entity.Post

class PostRepository: MyAPIRequest() {
    private val myAPI = MyServiceBuilder.buildService(PostAPI::class.java)

    suspend fun getAllPost(): MutableList<Post> {
        val response =  apiRequest {
            myAPI.getAllPost(MyServiceBuilder.token!!)
        }
        return response.data!!
    }
}