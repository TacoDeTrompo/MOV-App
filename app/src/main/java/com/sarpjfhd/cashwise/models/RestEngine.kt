package com.sarpjfhd.cashwise.models

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate


class RestEngine {
    companion object{
        fun getRestEngine(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val gson = GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter().nullSafe())
                .registerTypeAdapter(TransactionTypes::class.java, TransactionTypeAdapter().nullSafe())
                .registerTypeAdapter(ExpenseType::class.java, ExpenseTypeAdapter().nullSafe())
                .create()

            val client =  OkHttpClient.Builder().addInterceptor(interceptor).build()
            val retrofit =  Retrofit.Builder()
                    .baseUrl(ServerData.serverUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()

            return  retrofit

        }
    }
}