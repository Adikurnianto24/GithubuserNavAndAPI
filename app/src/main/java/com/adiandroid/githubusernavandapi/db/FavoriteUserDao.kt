package com.adiandroid.githubusernavandapi.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addToFavorite(userFavorite: FavoriteUser)

    @Query("SELECT * FROM favorite_users")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite_users WHERE favorite_users.id = :id")
    suspend fun checkUserFavorite(id: Int): Int

    @Query("DELETE FROM favorite_users WHERE favorite_users.id = :id")
    suspend fun removeUserFromFavorite(id: Int)
}