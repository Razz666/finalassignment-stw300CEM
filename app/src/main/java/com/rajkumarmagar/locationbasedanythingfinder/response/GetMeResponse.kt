package com.rajkumarmagar.locationbasedanythingfinder.response

import com.rajkumarmagar.locationbasedanythingfinder.entity.User

class GetMeResponse(
        val success: Boolean? = null,
        val me: User? = null
)