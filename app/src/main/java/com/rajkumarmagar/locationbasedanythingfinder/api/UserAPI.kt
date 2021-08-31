package com.rajkumarmagar.locationbasedanythingfinder.api

import com.rajkumarmagar.locationbasedanythingfinder.entity.User
import com.rajkumarmagar.locationbasedanythingfinder.response.CreateResponse
import com.rajkumarmagar.locationbasedanythingfinder.response.GetMeResponse
import com.rajkumarmagar.locationbasedanythingfinder.response.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {

    @POST("createUser/")
    suspend fun registerUser(
        @Body user: User
    ): Response<CreateResponse>

    @FormUrlEncoded
    @POST("login/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("/getMe")
    suspend fun getMe(
            @Header("Authorization") token: String
    ): Response<GetMeResponse>

    @Multipart
    @PUT("/updateUser")
    suspend fun updateUser(
            @Header("Authorization") token: String,
            @Part profileImage: MultipartBody.Part
    ): Response<CreateResponse>
}