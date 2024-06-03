package com.adiandroid.githubusernavandapi.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.adiandroid.githubusernavandapi.db.AppDatabase
import com.adiandroid.githubusernavandapi.db.FavoriteUser
import com.adiandroid.githubusernavandapi.db.FavoriteUserDao
import androidx.lifecycle.MutableLiveData

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao: FavoriteUserDao?
    private var userDb: AppDatabase?

    init {
        userDb = AppDatabase.getDatabase(application)
        userDao = userDb?.userFavoriteDao()
    }

    fun getUserFavorite(): LiveData<List<FavoriteUser>> {
        return userDao?.getFavoriteUser() ?: MutableLiveData<List<FavoriteUser>>()
    }
}
