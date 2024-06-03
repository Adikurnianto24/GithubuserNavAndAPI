package com.adiandroid.githubusernavandapi.api

import com.adiandroid.githubusernavandapi.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = BuildConfig.BASE_URL //ini Baseurl template dari api githubnya, saya merevisi agar url tidak ditempatkan di kelas
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInstance: Api = retrofit.create(Api::class.java)
}