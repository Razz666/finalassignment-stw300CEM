package com.rajkumarmagar.locationbasedanythingfinder.repository

import com.rajkumarmagar.locationbasedanythingfinder.api.MyAPIRequest
import com.rajkumarmagar.locationbasedanythingfinder.api.MyServiceBuilder
import com.rajkumarmagar.locationbasedanythingfinder.api.UserAPI
import com.rajkumarmagar.locationbasedanythingfinder.entity.User
import com.rajkumarmagar.locationbasedanythingfinder.response.CreateResponse
import com.rajkumarmagar.locationbasedanythingfinder.response.GetMeResponse
import com.rajkumarmagar.locationbasedanythingfinder.response.LoginResponse
import okhttp3.MultipartBody

class UserRepository: MyAPIRequest() {
    private val myAPI = MyServiceBuilder.buildService(UserAPI::class.java)

    suspend fun registerUser(user: User): CreateResponse {
        return apiRequest {
            myAPI.registerUser(user)
        }
    }

    suspend fun login(username: String, password: String): LoginResponse {
        return apiRequest {
            myAPI.login(username, password)
        }
    }

    suspend fun getMe(): GetMeResponse {
        return apiRequest {
            myAPI.getMe(MyServiceBuilder.token!!)
        }
    }

    suspend fun updateUser(body: MultipartBody.Part): CreateResponse {
        return apiRequest {
            myAPI.updateUser(MyServiceBuilder.token!!, body)
        }
    }
}