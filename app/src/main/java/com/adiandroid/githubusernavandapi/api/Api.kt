package com.adiandroid.githubusernavandapi.api

import com.adiandroid.githubusernavandapi.model.DetailUserResponse
import com.adiandroid.githubusernavandapi.model.User
import com.adiandroid.githubusernavandapi.model.UserResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.adiandroid.githubusernavandapi.BuildConfig


interface Api {

    @GET("search/users")
    fun getSearchUser(
        @Query("q") query : String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ) : Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    companion object {
        private val BASE_URL = BuildConfig.BASE_URL //saya merevisi base url dan api token dipindahkan ke dalam build.gradle

        fun create(): Api {
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "token " + BuildConfig.apiKey)
                    .build()
                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }
}

