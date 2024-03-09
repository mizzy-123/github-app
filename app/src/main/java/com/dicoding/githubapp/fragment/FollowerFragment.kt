package com.dicoding.githubapp.fragment

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapp.BuildConfig
import com.dicoding.githubapp.DetailActivity
import com.dicoding.githubapp.R
import com.dicoding.githubapp.adapter.ListDataFollower
import com.dicoding.githubapp.api.response.DataFollowers
import com.dicoding.githubapp.databinding.FragmentFollowerBinding
import com.dicoding.githubapp.model.FollowViewModel

class FollowerFragment : Fragment() {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!

    private lateinit var followViewModel: FollowViewModel

    private lateinit var listDataFollower: ListDataFollower

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)

        followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowViewModel::class.java)

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = DetailActivity.username
        val token: String = BuildConfig.API_KEY
        followViewModel.getDataFollower(username, token)

        followViewModel.isLoading.observe(requireActivity()){
            if (it){
                binding.also {
                    it.shimmerFollower.startShimmer()
                    it.shimmerFollower.visibility = View.VISIBLE
                    it.rvFollower.visibility = View.INVISIBLE
                }
            }else {
                binding.also {
                    it.shimmerFollower.stopShimmer()
                    it.shimmerFollower.visibility = View.INVISIBLE
                    it.rvFollower.visibility = View.VISIBLE

                    followViewModel.follower.observe(requireActivity()){ dataFollower ->
                        Log.d("Follower", dataFollower.toString())
                        showRecycleList(dataFollower)
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showRecycleList(list: List<DataFollowers>){
        val recycleGithub = binding.rvFollower
        recycleGithub.layoutManager = LinearLayoutManager(requireContext())
        listDataFollower = ListDataFollower(list)
        recycleGithub.adapter = listDataFollower
        listDataFollower.setOnClickCallback(object : ListDataFollower.OnItemClickCallback {
            override fun onItemClicked(data: DataFollowers, position: Int, viewAdapter: View) {
                Toast.makeText(requireContext(), "Anda menekan ${data.login}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}