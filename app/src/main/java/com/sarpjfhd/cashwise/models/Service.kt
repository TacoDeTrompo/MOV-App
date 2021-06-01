package com.sarpjfhd.cashwise.models
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @GET("Profiles/{id}")
    fun getProfile(@Path("id") id: Int): Call<List<Profile>>

    @GET("Profiles")
    fun getProfiles(): Call<List<Profile>>

    @Headers("Content-Type: application/json")
    @POST("LogIn")
    fun logIn(@Body userData: User): Call<Int>
}