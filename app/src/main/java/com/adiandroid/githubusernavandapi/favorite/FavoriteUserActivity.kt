// FavoriteUserActivity.kt
package com.adiandroid.githubusernavandapi.favorite

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adiandroid.githubusernavandapi.databinding.ActivityFavoriteUserBinding
import com.adiandroid.githubusernavandapi.detail.DetailUser
import com.adiandroid.githubusernavandapi.db.FavoriteUser
import com.adiandroid.githubusernavandapi.main.ListDataAdapter
import com.adiandroid.githubusernavandapi.model.User
import com.adiandroid.githubusernavandapi.settings.SettingsActivity

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: ListDataAdapter
    private lateinit var viewModel: FavoriteViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListDataAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        adapter.setOnItemClickCallBack(object : ListDataAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@FavoriteUserActivity, DetailUser::class.java).also {
                    it.putExtra(DetailUser.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUser.EXTRA_ID, data.id)
                    it.putExtra(DetailUser.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(it)
                }
            }
        })


        binding.settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.apply {
            rvUserFav.setHasFixedSize(true)
            rvUserFav.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            rvUserFav.adapter = adapter
            backHome.setOnClickListener {
                finish()
            }
        }

        viewModel.getUserFavorite()?.observe(this) { favoriteUsers ->
            if (favoriteUsers != null) {
                val userList = mapList(favoriteUsers)
                adapter.setList(userList)
            }
        }
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (user in users) {
            val userMapped = User(
                user.login,
                user.id,
                user.avatar_url,
                user.html_url
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}