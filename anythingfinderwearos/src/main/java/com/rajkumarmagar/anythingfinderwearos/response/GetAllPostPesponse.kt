package com.rajkumarmagar.anythingfinderwearos.response

import com.rajkumarmagar.anythingfinderwearos.entity.Post

data class GetAllPostResponse(
    val success: Boolean? = null,
    val data: ArrayList<Post>? = null
)