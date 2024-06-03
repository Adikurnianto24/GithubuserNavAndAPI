package com.adiandroid.githubusernavandapi.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adiandroid.githubusernavandapi.R
import com.adiandroid.githubusernavandapi.databinding.ActivityMainBinding
import com.adiandroid.githubusernavandapi.detail.DetailUser
import com.adiandroid.githubusernavandapi.favorite.FavoriteUserActivity
import com.adiandroid.githubusernavandapi.model.User
import com.adiandroid.githubusernavandapi.settings.SettingsActivity
import com.adiandroid.githubusernavandapi.settings.SettingsPreferences
import com.adiandroid.githubusernavandapi.settings.dataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ListDataAdapter
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        adapter = ListDataAdapter()

        adapter.setOnItemClickCallBack(object : ListDataAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUser::class.java).also {
                    it.putExtra(DetailUser.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUser.EXTRA_ID, data.id)
                    it.putExtra(DetailUser.EXTRA_AVATAR, data.avatarUrl)
                    it.putExtra(DetailUser.EXTRA_URL, data.htmlUrl)
                    startActivity(it)
                }
            }
        })

        val pref = SettingsPreferences.getInstance(this.dataStore)
        pref.getThemeSetting().asLiveData().observeForever { isDarkModeActive: Boolean ->
            val mode = if (isDarkModeActive) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.apply {
            userRv.layoutManager = LinearLayoutManager(this@MainActivity)
            userRv.setHasFixedSize(true)
            userRv.adapter = adapter

            userSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        searchUser(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun searchUser(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            showLoading(true)
            viewModel.setSearchUsers(query) { message ->
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
            delay(1000)
            showLoading(false)
            val users = viewModel.listUsers.value
            if (users.isNullOrEmpty()) {
                binding.userRv.visibility = View.GONE
                binding.imageViewNotFound.visibility = View.VISIBLE
            } else {
                binding.userRv.visibility = View.VISIBLE
                binding.imageViewNotFound.visibility = View.GONE
                adapter.setList(users)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.favorite -> {
                val intent = Intent(this, FavoriteUserActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
