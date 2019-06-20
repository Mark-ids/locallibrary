package com.qathafiii.locallibrary.screens.interfaces.networkinterfaces

import com.qathafiii.locallibrary.screens.models.PostUserResponse
import com.qathafiii.locallibrary.screens.models.User
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call



interface LocalApiService {

    @POST("user")
    fun postUser(@Body user: User):Call<PostUserResponse>

}