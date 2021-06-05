package com.sarpjfhd.cashwise.models
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @Headers("Content-Type: application/json")
    @POST("getProfile.php")
    fun getProfile(@Body profile: Profile): Call<Profile>

    @Headers("Content-Type: application/json")
    @POST("deleteProfile.php")
    fun deleteProfile(profile: Profile): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("getProfiles.php")
    fun getProfiles(@Body user: User): Call<List<Profile>>

    @Headers("Content-Type: application/json")
    @POST("getExpense.php")
    fun getExpenses(transactionData: TransactionData): Call<Expense>

    @Headers("Content-Type: application/json")
    @POST("transactionDelete.php")
    fun deleteTransaction(transactionData: TransactionData): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("transactionInsert.php")
    fun createTransaction(transactionData: TransactionData): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("transactionUpdate.php")
    fun updateTransaction(transactionData: TransactionData): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("getIngress.php")
    fun getIngress(transactionData: TransactionData): Call<Ingress>

    @Headers("Content-Type: application/json")
    @POST("getExpenses.php")
    fun getExpensesByProfile(profile: Profile): Call<List<Expense>>

    @Headers("Content-Type: application/json")
    @POST("getIngresses.php")
    fun getIngressesById(profile: Profile): Call<List<Ingress>>

    @GET("userdata/{id}")
    fun getUserData(@Path("id") id: Int): Call<User>

    @GET("getAdvices.php")
    fun getAdvices(): Call<List<Advice>>

    @GET("advice/{id}")
    fun getAdvices(@Path("id") id: Int): Call<String>

    @Headers("Content-Type: application/json")
    @POST("checkToken")
    fun checkToken(@Body token: String): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("login.php")
    fun logIn(@Body userData: LogInData): Call<User>

    @Headers("Content-Type: application/json")
    @POST("signup.php")
    fun signUp(@Body userData: User): Call<User>

    @Headers("Content-Type: application/json")
    @POST("profileInsert.php")
    fun createProfile(@Body profile: Profile): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("updateProfile.php")
    fun updateProfile(@Body profile: Profile): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("updateTransaction")
    fun updateExpense(@Body expense: Expense): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("updateTransaction")
    fun updateIngress(@Body ingress: Ingress): Call<Boolean>

    @Headers("Content-Type: application/json")
    @POST("updateUserData.php")
    fun updateUser(@Body user: User): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("updatePassword.php")
    fun resetPassword(@Body passwordReset: PasswordReset): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("insertAdvices.php")
    fun uploadAdvice(@Body advice: Advice): Call<ResponseBody>
}