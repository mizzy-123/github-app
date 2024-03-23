package com.dicoding.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.dicoding.githubapp.databinding.ActivitySettingBinding
import com.dicoding.githubapp.helper.SettingPreferences
import com.dicoding.githubapp.helper.dataStore
import com.dicoding.githubapp.model.SettingModelFactory
import com.dicoding.githubapp.model.SettingViewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingModelFactory(pref)).get(SettingViewModel::class.java)

        settingViewModel.getThemeSettings().observe(this){ isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.toolBar.setNavigationIconTint(resources.getColor(R.color.white))
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.toolBar.setNavigationIconTint(resources.getColor(R.color.black))
                binding.switchTheme.isChecked = false
            }
        }
        binding.also {
            it.toolBar.setNavigationOnClickListener {
                finish()
            }

            it.switchTheme.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                settingViewModel.saveThemeSetting(isChecked)
            }
        }
    }
}