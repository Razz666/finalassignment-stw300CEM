package com.rajkumarmagar.locationbasedanythingfinder.api

import com.rajkumarmagar.locationbasedanythingfinder.entity.Post
import com.rajkumarmagar.locationbasedanythingfinder.response.CreateResponse
import com.rajkumarmagar.locationbasedanythingfinder.response.GetAllPostResponse
import com.rajkumarmagar.locationbasedanythingfinder.response.GetPostResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostAPI {

    @GET ("/getAllPost")
    suspend fun getAllPost(
        @Header("Authorization") token: String
    ): Response<GetAllPostResponse>

    @GET ("/getMyPost")
    suspend fun getMyPost(
            @Header("Authorization") token: String
    ): Response<GetAllPostResponse>

    @DELETE("/deletePost/{id}")
    suspend fun deletePost(
            @Header("Authorization") token: String,
            @Path("id") id: String
    ): Response<CreateResponse>

    @GET("/getPost/{id}")
    suspend fun getPost(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<GetPostResponse>

    @POST("/createAPost")
    suspend fun createPost(
            @Header("Authorization") token: String,
            @Body post: Post
    ): Response<CreateResponse>

    @Multipart
    @PUT("/uploadPostPhoto/{id}")
    suspend fun uploadPostPhoto(
            @Header("Authorization") token: String,
            @Part postPhoto: MultipartBody.Part,
            @Path("id") id: String
    ): Response<CreateResponse>

    @PUT("/updateAPost")
    suspend fun updatePost(
        @Header("Authorization") token: String,
        @Body post: Post
    ): Response<CreateResponse>
}