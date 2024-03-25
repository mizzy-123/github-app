package com.dicoding.githubapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapp.adapter.ListFavouriteAdapter
import com.dicoding.githubapp.database.Favourite
import com.dicoding.githubapp.databinding.ActivityFavouriteBinding
import com.dicoding.githubapp.helper.SettingPreferences
import com.dicoding.githubapp.helper.dataStore
import com.dicoding.githubapp.model.FavouriteViewModel
import com.dicoding.githubapp.model.SettingModelFactory
import com.dicoding.githubapp.model.SettingViewModel
import com.dicoding.githubapp.model.ViewModelFactory

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding
    lateinit var listFavouriteAdapter: ListFavouriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favouriteViewModel = obtainViewModel(this@FavouriteActivity)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingModelFactory(pref)).get(SettingViewModel::class.java)

        settingViewModel.getThemeSettings().observe(this){ isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                binding.toolBar.setNavigationIconTint(resources.getColor(R.color.white))
            } else {
                binding.toolBar.setNavigationIconTint(resources.getColor(R.color.black))
            }
        }

        favouriteViewModel.getAllFavourite().observe(this){ listFavourite ->
            showRecycleList(listFavourite)
        }

        binding.toolBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun showRecycleList(listFavourite: List<Favourite>) {
        val recyclerGithub = binding.rvGithub
        recyclerGithub.layoutManager = LinearLayoutManager(this)
        listFavouriteAdapter = ListFavouriteAdapter(listFavourite)
        recyclerGithub.adapter = listFavouriteAdapter
        listFavouriteAdapter.setOnClickCallback(object : ListFavouriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favourite) {
                val intent = Intent(this@FavouriteActivity, DetailActivity::class.java)
                intent.putExtra("username", data.login)
                intent.putExtra("id", data.id)
                intent.putExtra("avatar", data.avatar_url)
                startActivity(intent)
            }

        })
    }

    private fun obtainViewModel(activitiy: AppCompatActivity): FavouriteViewModel {
        val factory = ViewModelFactory.getInstance(activitiy.application)
        return ViewModelProvider(activitiy, factory).get(FavouriteViewModel::class.java)
    }
}