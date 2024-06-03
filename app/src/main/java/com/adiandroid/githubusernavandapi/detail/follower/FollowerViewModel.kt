package com.adiandroid.githubusernavandapi.detail.follower

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adiandroid.githubusernavandapi.api.RetrofitClient
import com.adiandroid.githubusernavandapi.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel : ViewModel() {
    val listFollowers = MutableLiveData<ArrayList<User>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun setListFollowers(username: String, showToast: (String) -> Unit) {
        _isLoading.value = true
        RetrofitClient.apiInstance
            .getUserFollowers(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        listFollowers.postValue(response.body())
                    } else {
                        val errorMessage = "Failed to fetch followers. Please try again."
                        _errorMessage.postValue(errorMessage)
                        showToast(errorMessage)
                    }
                }
                //Saya merevisi untuk menambahkan handle event error jika tidak ada internet
                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    _isLoading.value = false
                    val errorMessage = "Network error occurred. Please check your connection and try again."
                    Log.d("nama", "Try again, Fetching Failed", t)
                    _errorMessage.postValue(errorMessage)
                    showToast(errorMessage)
                }
            })
    }
}
