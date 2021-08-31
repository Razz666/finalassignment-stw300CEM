package com.rajkumarmagar.anythingfinderwearos.repository

import com.rajkumarmagar.anythingfinderwearos.api.MyAPIRequest
import com.rajkumarmagar.anythingfinderwearos.api.MyServiceBuilder
import com.rajkumarmagar.anythingfinderwearos.api.UserAPI
import com.rajkumarmagar.anythingfinderwearos.response.LoginResponse

class UserRepository: MyAPIRequest() {
    private val myAPI = MyServiceBuilder.buildService(UserAPI::class.java)

    suspend fun login(username: String, password: String): LoginResponse {
        return apiRequest {
            myAPI.login(username, password)
        }
    }
}