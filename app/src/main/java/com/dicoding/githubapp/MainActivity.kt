package com.dicoding.githubapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapp.adapter.ListDataSearchAdapter
import com.dicoding.githubapp.api.response.DataSearchItems
import com.dicoding.githubapp.databinding.ActivityMainBinding
import com.dicoding.githubapp.helper.SettingPreferences
import com.dicoding.githubapp.helper.dataStore
import com.dicoding.githubapp.model.MainViewModel
import com.dicoding.githubapp.model.SettingModelFactory
import com.dicoding.githubapp.model.SettingViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var listDataSearchAdapter: ListDataSearchAdapter
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingModelFactory(pref)).get(
            SettingViewModel::class.java)
        settingViewModel.getThemeSettings().observe(this){ isDarkModeActive: Boolean ->
            val menuIcon = binding.toolBar.menu.findItem(R.id.favourite)
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    menuIcon.setIconTintList(ContextCompat.getColorStateList(this, R.color.white))
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    menuIcon.setIconTintList(ContextCompat.getColorStateList(this, R.color.black))
                }
            }
        }

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val token: String = BuildConfig.API_KEY
                    try {
                        MainScope().launch {
                            val defferedSearch = async {
                                mainViewModel.searchGithub(query, token)
                            }
                            val getDataSearch = defferedSearch.await()
                            Log.d("masuk", getDataSearch.toString())
                            listDataSearchAdapter.setListDataSearch(getDataSearch.items)
                        }
                    } catch (e: Exception){
                        Toast.makeText(this@MainActivity, "Ops.. something wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        showRecycleList()

        mainViewModel.isLoading.observe(this){
            if(it){
                binding.conStart.visibility = View.INVISIBLE
                binding.shimmerViewContainer.startShimmer()
                binding.shimmerViewContainer.visibility = View.VISIBLE
                binding.rvGithub.visibility = View.INVISIBLE
            } else {
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.visibility = View.INVISIBLE
                binding.rvGithub.visibility = View.VISIBLE
                showRecycleList()
            }
        }

        binding.toolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId){
                R.id.favourite -> {
                    true
                }
                R.id.setting -> {
                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun showRecycleList(){
        val recycleGithub = binding.rvGithub
        recycleGithub.layoutManager = LinearLayoutManager(this)
        listDataSearchAdapter = ListDataSearchAdapter()
        recycleGithub.adapter = listDataSearchAdapter
        listDataSearchAdapter.setOnClickCallback(object : ListDataSearchAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataSearchItems, position: Int, viewAdapter: View) {
                val intent = Intent(viewAdapter.context, DetailActivity::class.java)
                intent.putExtra("username", data.login)
                startActivity(intent)
            }

        })
    }
}