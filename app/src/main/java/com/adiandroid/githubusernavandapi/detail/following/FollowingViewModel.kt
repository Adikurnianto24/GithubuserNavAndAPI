package com.adiandroid.githubusernavandapi.detail.following

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adiandroid.githubusernavandapi.api.RetrofitClient
import com.adiandroid.githubusernavandapi.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val _listFollowing = MutableLiveData<ArrayList<User>>()
    val listFollowing: LiveData<ArrayList<User>> get() = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        _isLoading.value = false
    }

    fun setListFollowing(username: String, showToast: (String) -> Unit) {
        _isLoading.value = true
        RetrofitClient.apiInstance.getUserFollowing(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        _listFollowing.postValue(response.body())
                    } else {
                        showToast("Failed to fetch following users. Please try again.")
                    }
                    _isLoading.postValue(false)
                }
                //Saya melakukan hal sama seperti sebelumnya yaitu pada follower
                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    showToast("Network error occurred. Please check your connection and try again.")
                    _isLoading.postValue(false)
                }
            })
    }
}
