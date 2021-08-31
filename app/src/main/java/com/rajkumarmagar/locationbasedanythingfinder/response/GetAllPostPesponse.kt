package com.rajkumarmagar.locationbasedanythingfinder.response

import com.rajkumarmagar.locationbasedanythingfinder.entity.Post

data class GetAllPostResponse(
    val success: Boolean? = null,
    val data: ArrayList<Post>? = null
)