package com.example.secure.Retrofit


import com.example.secure.Retrofit.Root
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("foundationModels/v1/completion")
    fun gpt(@Header("Authorization") auth: String, @Body data: RequestBody): Call<Root>
}