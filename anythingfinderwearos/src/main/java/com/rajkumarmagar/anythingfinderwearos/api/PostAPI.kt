package com.rajkumarmagar.anythingfinderwearos.api

import com.rajkumarmagar.anythingfinderwearos.response.GetAllPostResponse
import retrofit2.Response
import retrofit2.http.*

interface PostAPI {

    @GET ("/getAllPost")
    suspend fun getAllPost(
        @Header("Authorization") token: String
    ): Response<GetAllPostResponse>
}