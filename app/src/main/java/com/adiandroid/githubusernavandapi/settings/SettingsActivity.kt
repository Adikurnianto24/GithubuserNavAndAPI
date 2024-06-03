package com.adiandroid.githubusernavandapi.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.adiandroid.githubusernavandapi.R
import com.adiandroid.githubusernavandapi.databinding.ActivitySettingsBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private var isDarkModeActive= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backHome.setOnClickListener {
            finish()
        }

        val switchTheme: SwitchMaterial = findViewById(R.id.switch_theme)

        val pref = SettingsPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingsViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this) { themeSetting: Boolean ->
            isDarkModeActive = themeSetting
            switchTheme.isChecked = isDarkModeActive
            updateAppTheme()
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != isDarkModeActive) {
                isDarkModeActive = isChecked
                mainViewModel.saveThemeSetting(isDarkModeActive)
                val toastMessage = if (isChecked) "Dark Mode" else "Light Mode"
                Toast.makeText(this@SettingsActivity, toastMessage, Toast.LENGTH_SHORT).show()
                updateAppTheme()
            }
        }
    }

    private fun updateAppTheme() {
        val mode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}