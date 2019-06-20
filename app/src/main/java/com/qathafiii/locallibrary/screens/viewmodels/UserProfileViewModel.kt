package com.qathafiii.locallibrary.screens.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qathafiii.locallibrary.screens.interfaces.networkinterfaces.LocalApiService
import com.qathafiii.locallibrary.screens.models.PostUserResponse
import com.qathafiii.locallibrary.screens.models.User
import com.qathafiii.locallibrary.screens.network.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserProfileViewModel() : ViewModel(),Callback<PostUserResponse>{
    var user = User("","","","")
    val response: MutableLiveData<String> = MutableLiveData()

    fun registerUser(){
        Log.e("RegisterCall", "Called")
        val client = Retrofit().getClient()
        val call = client.create(LocalApiService::class.java).postUser(user)

        call.enqueue(this)

    }

    override fun onResponse(call: Call<PostUserResponse>, response1: Response<PostUserResponse>) {
         response.value = response1.body()?.message
        Log.e("RegisterResponse","anything anything anything")
    }

    override fun onFailure(call: Call<PostUserResponse>, t: Throwable) {
        // Log error here since request failed
        Log.e("RegisterResponse","anything anything anything")
        t.printStackTrace()
    }
}