package com.adiandroid.githubusernavandapi.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adiandroid.githubusernavandapi.api.RetrofitClient
import com.adiandroid.githubusernavandapi.model.User
import com.adiandroid.githubusernavandapi.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<ArrayList<User>>()
    val listUsers: LiveData<ArrayList<User>> get() = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun setSearchUsers(query: String, showToast: (String) -> Unit) {
        _isLoading.value = true
        RetrofitClient.apiInstance
            .getSearchUser(query)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listUsers.postValue(response.body()?.items)
                    } else {
                        val errorMessage = "Failed to fetch users. Please try again."
                        _errorMessage.postValue(errorMessage)
                        showToast(errorMessage)
                    }
                }
                //Saya juga merevisi menambahkan handle event seperti di follower & following
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.value = false
                    val errorMessage = "Network error occurred. Please check your connection and try again."
                    Log.d("nama", "Try again. Failed to get User", t)
                    _errorMessage.postValue(errorMessage)
                    showToast(errorMessage)
                }

            })
    }
}
