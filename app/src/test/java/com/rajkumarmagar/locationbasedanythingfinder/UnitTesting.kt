package com.rajkumarmagar.locationbasedanythingfinder

import com.rajkumarmagar.locationbasedanythingfinder.api.MyServiceBuilder
import com.rajkumarmagar.locationbasedanythingfinder.entity.User
import com.rajkumarmagar.locationbasedanythingfinder.repository.PostRepository
import com.rajkumarmagar.locationbasedanythingfinder.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UnitTesting {
    private lateinit var userRepository: UserRepository
    private lateinit var postRepository: PostRepository

    @Test
    fun checkLogin() = runBlocking {
        userRepository = UserRepository()
        val response = userRepository.login("qwerty", "123")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkSignup() = runBlocking {
        val user = User(username = "ujjal", email = "123456@gmail.com", password = "123")
        userRepository = UserRepository()
        val response = userRepository.registerUser(user)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun deletePost() = runBlocking {
        postRepository = PostRepository()
        userRepository = UserRepository()
        MyServiceBuilder.token ="Bearer " + userRepository.login("qwerty","123").token
        val response = postRepository.deletePost("612cf89a02725c1c1410ab79")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getMe() = runBlocking {
        userRepository = UserRepository()
        MyServiceBuilder.token ="Bearer " + userRepository.login("qwerty","123").token
        val response = userRepository.getMe()
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }
}