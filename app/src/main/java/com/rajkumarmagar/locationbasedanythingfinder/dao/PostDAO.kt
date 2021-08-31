package com.rajkumarmagar.locationbasedanythingfinder.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rajkumarmagar.locationbasedanythingfinder.entity.Post

@Dao
interface PostDAO {
//    @Insert
//    suspend fun signUpUser(user: User)
//
//    @Query("select * from User where username=(:username) and password=(:password)")
//    suspend fun logInUser(username: String, password: String): User

    @Insert
    suspend fun insertPost(allPost: ArrayList<Post>)

    @Query("select * from Post")
    suspend fun getPost(): MutableList<Post>
}