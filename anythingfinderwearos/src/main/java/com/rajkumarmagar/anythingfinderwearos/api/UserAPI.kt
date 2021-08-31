package com.rajkumarmagar.anythingfinderwearos.api

import com.rajkumarmagar.anythingfinderwearos.response.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    @FormUrlEncoded
    @POST("login/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>
}