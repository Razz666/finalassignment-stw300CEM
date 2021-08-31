package com.rajkumarmagar.anythingfinderwearos.response

import com.rajkumarmagar.anythingfinderwearos.entity.User

class LoginResponse(
    val success: Boolean? = null,
    val token: String? = null,
    val clientData: User? = null
)
