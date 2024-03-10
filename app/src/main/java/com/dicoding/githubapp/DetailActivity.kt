package com.dicoding.githubapp

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubapp.adapter.SectionPageAdapter
import com.dicoding.githubapp.databinding.ActivityDetailBinding
import com.dicoding.githubapp.model.DetailViewModel
import com.google.android.material.tabs.TabLayout

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var adapter: SectionPageAdapter
    private lateinit var detailViewModel: DetailViewModel



    companion object {
        var username: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras

        if (bundle !== null){
            username = bundle.getString("username", "")
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
}