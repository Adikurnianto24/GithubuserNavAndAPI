package com.adiandroid.githubusernavandapi.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adiandroid.githubusernavandapi.api.RetrofitClient
import com.adiandroid.githubusernavandapi.db.AppDatabase
import com.adiandroid.githubusernavandapi.db.FavoriteUserDao
import com.adiandroid.githubusernavandapi.model.DetailUserResponse
import com.adiandroid.githubusernavandapi.db.FavoriteUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao: FavoriteUserDao?
    private var userDb: AppDatabase?

    private val user = MutableLiveData<DetailUserResponse>()
    private val loading = MutableLiveData<Boolean>()
    private val error = MutableLiveData<String>()

    init {
        userDb = AppDatabase.getDatabase(application)
        userDao = userDb?.userFavoriteDao()
    }

    fun setUserDetail(username: String) {
        loading.value = true
        RetrofitClient.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                    loading.value = false
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    } else {
                        error.postValue("Failed to fetch user details: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    loading.value = false
                    error.postValue("Failed to fetch user details: ${t.message}") //Revisi sebelumnya tidak ada handle ketika error
                }
            })
    }

    fun getUserDetail(): LiveData<DetailUserResponse> {
        return user
    }

    fun getLoadingStatus(): LiveData<Boolean> {
        return loading
    }

    fun getError(): LiveData<String> {
        return error
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String, htmlUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userDao?.addToFavorite(FavoriteUser(username, id, avatarUrl, htmlUrl))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUserFavorite(id)

    fun removeUserFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeUserFromFavorite(id)
        }
    }
}