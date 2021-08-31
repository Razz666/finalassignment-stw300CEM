package com.rajkumarmagar.locationbasedanythingfinder.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
        val _id: String? = null,
        val username: String? = null,
        val postCaption: String? = null,
        val postPhoto: String? = null,
        val latitude: String? = null,
        val longitude: String? = null,
        val contact: String? = null
) {
    @PrimaryKey
    var postID: Int? = null
}