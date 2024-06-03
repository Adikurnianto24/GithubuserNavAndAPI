package com.adiandroid.githubusernavandapi.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.adiandroid.githubusernavandapi.R
import com.adiandroid.githubusernavandapi.databinding.ActivityDetailUserBinding
import com.adiandroid.githubusernavandapi.settings.SettingsActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUser : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_URL = "extra_url"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        val username = intent?.getStringExtra(EXTRA_USERNAME) ?: ""
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent?.getStringExtra(EXTRA_AVATAR) ?: ""
        val url = intent?.getStringExtra(EXTRA_URL) ?: ""
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        binding.progressBar.visibility = View.VISIBLE
        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        var isChecked = false

        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null) {
                    if (count > 0) {
                        binding.favoriteToggle.isChecked = true
                        isChecked = true
                    } else {
                        binding.favoriteToggle.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        if (username != null) {
            viewModel.setUserDetail(username)
            viewModel.getUserDetail().observe(this) {
                if (it != null) {
                    binding.apply {
                        nameTv.text = it.name
                        usernameTv.text = it.login
                        followersTv.text = resources.getString(R.string.follower_count, it.followers)
                        followingTv.text = resources.getString(R.string.following_count, it.following)

                        Glide.with(this@DetailUser)
                            .load(it.avatarUrl)
                            .into(userCiv)
                        progressBar.visibility = View.GONE
                    }

                }
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabLayout.setupWithViewPager(viewPager)
        }

        val btnBack = binding.toolbar.findViewById<ImageButton>(R.id.btn_back)
        btnBack?.setOnClickListener {
            super.onBackPressed()
        }

        binding.favoriteToggle.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                if (username != null) {
                    if (avatar != null) {
                        if (url != null) {
                            viewModel.addToFavorite(username, id, avatar, url)
                            Toast.makeText(this, "Berhasil ditambahkan ke Favorite", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                viewModel.removeUserFromFavorite(id)
                Toast.makeText(this, "Berhasil dihapus dari Favorite", Toast.LENGTH_SHORT).show()
            }
            binding.favoriteToggle.isChecked = isChecked
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val favoriteItem = menu?.findItem(R.id.favorite)
        favoriteItem?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }}
