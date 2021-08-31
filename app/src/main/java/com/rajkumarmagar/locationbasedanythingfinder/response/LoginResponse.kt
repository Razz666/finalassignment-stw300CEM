package com.rajkumarmagar.locationbasedanythingfinder.response

import com.rajkumarmagar.locationbasedanythingfinder.entity.User

class LoginResponse(
    val success: Boolean? = null,
    val token: String? = null,
    val clientData: User? = null
)
