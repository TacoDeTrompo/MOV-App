package com.sarpjfhd.cashwise.models
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @GET("profiles/{id}")
    fun getProfile(@Path("id") id: Int): Call<Profile>

    @GET("profiles")
    fun getProfiles(): Call<List<Profile>>

    @GET("expenses/{id}")
    fun getExpenses(@Path("id") id: Int): Call<Expense>

    @GET("ingress/{id}")
    fun getIngress(@Path("id") id: Int): Call<Ingress>

    @GET("expenses")
    fun getExpenses(): Call<List<Expense>>

    @GET("ingress")
    fun getIngresses(): Call<List<Ingress>>

    @GET("userdata/{id}")
    fun getUserData(@Path("id") id: Int): Call<User>

    @GET("advice")
    fun getAdvice(): Call<List<String>>

    @GET("advice/{id}")
    fun getAdvices(@Path("id") id: Int): Call<String>

    @Headers("Content-Type: application/json")
    @POST("checkToken")
    fun checkToken(@Body token: String): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun logIn(@Body userData: User): Call<String>
}