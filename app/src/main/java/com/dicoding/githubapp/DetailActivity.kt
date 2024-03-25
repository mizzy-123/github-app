package com.dicoding.githubapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubapp.adapter.SectionPageAdapter
import com.dicoding.githubapp.database.Favourite
import com.dicoding.githubapp.databinding.ActivityDetailBinding
import com.dicoding.githubapp.helper.SettingPreferences
import com.dicoding.githubapp.helper.dataStore
import com.dicoding.githubapp.model.DetailViewModel
import com.dicoding.githubapp.model.FavouriteViewModel
import com.dicoding.githubapp.model.SettingModelFactory
import com.dicoding.githubapp.model.SettingViewModel
import com.dicoding.githubapp.model.ViewModelFactory
import com.google.android.material.tabs.TabLayout

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var adapter: SectionPageAdapter
    private lateinit var detailViewModel: DetailViewModel



    companion object {
        var username: String = ""
        var id: Long = 0L
        var count: Int = 0
        var avatar: String = ""
        val favourite = Favourite()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val favouriteViewModel = obtainViewModel(this@DetailActivity)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingModelFactory(pref)).get(SettingViewModel::class.java)

        settingViewModel.getThemeSettings().observe(this){ isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                binding.toolBar.setNavigationIconTint(resources.getColor(R.color.white))
            } else {
                binding.toolBar.setNavigationIconTint(resources.getColor(R.color.black))
            }
        }

        if (bundle !== null){
            username = bundle.getString("username", "")
            id = bundle.getLong("id")
            avatar = bundle.getString("avatar", "")
        }

        modelDetail()

        adapter = SectionPageAdapter(supportFragmentManager, lifecycle)
        binding.also {
            it.viewPager.adapter = adapter
            it.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        binding.viewPager.currentItem = tab.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })

            it.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    it.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                }
            })

            it.toolBar.setNavigationOnClickListener {
                finish()
            }

            favouriteViewModel.cekFavourite(username).observe(this){ c ->
                count = c
                Log.d("count", c.toString())
                if (c > 0){
                    it.fab.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_baseline_favorite_black))
                } else {
                    it.fab.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_baseline_favorite_border))
                }
            }

            it.fab.setOnClickListener {
                favourite.let { f ->
                    f.login = username
                    f.avatar_url = avatar
                }
                if (count > 0){
                    favouriteViewModel.deleteByUsername(username)
                } else {
                    favouriteViewModel.insert(favourite)
                }
                Log.d("fab", "masuk")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun modelDetail(){
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        detailViewModel.isLoading.observe(this){
            if(it){
                binding.also {
                    it.shimmerDetail.startShimmer()
                    it.shimmerDetail.visibility = View.VISIBLE
                    it.conDetail.visibility = View.INVISIBLE
                }
            } else {
                binding.also {
                    it.shimmerDetail.stopShimmer()
                    it.shimmerDetail.visibility = View.INVISIBLE
                    it.conDetail.visibility = View.VISIBLE

                    detailViewModel.detailAccount.observe(this){ dataDetail ->
                        Glide.with(this@DetailActivity)
                            .load(dataDetail.avatar_url)
                            .into(it.avatar)

                        it.follower.setText("Follower: ${dataDetail.followers}")
                        it.following.setText("Following: ${dataDetail.following}")
                        it.repository.setText("Repository: ${dataDetail.public_repos}")
                        it.loginName.text = dataDetail.login
                        it.name.text = dataDetail.name
                    }
                }
            }
        }

    }

    private fun obtainViewModel(activitiy: AppCompatActivity): FavouriteViewModel {
        val factory = ViewModelFactory.getInstance(activitiy.application)
        return ViewModelProvider(activitiy, factory).get(FavouriteViewModel::class.java)
    }
}