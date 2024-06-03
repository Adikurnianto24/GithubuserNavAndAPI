package com.adiandroid.githubusernavandapi.model

import com.google.gson.annotations.SerializedName

data class DetailUserResponse (
    //Revisi menggunakan SerializedName
    @SerializedName("login") val login: String,
    @SerializedName("id") val id: Int,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("followers_url") val followersUrl: String,
    @SerializedName("following_url") val followingUrl: String,
    @SerializedName("name") val name: String,
    @SerializedName("followers") val followers: Int,
    @SerializedName("following") val following: Int,
)
