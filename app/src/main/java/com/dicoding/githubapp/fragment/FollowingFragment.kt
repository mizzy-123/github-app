package com.dicoding.githubapp.fragment

import android.content.res.Resources
import android.os.Bundle
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
import com.dicoding.githubapp.adapter.ListDataFollowing
import com.dicoding.githubapp.api.response.DataFollowing
import com.dicoding.githubapp.databinding.FragmentFollowingBinding
import com.dicoding.githubapp.model.FollowViewModel

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followViewModel: FollowViewModel
    private lateinit var listDataFollowing: ListDataFollowing

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowViewModel::class.java)

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = DetailActivity.username
        val token: String = BuildConfig.API_KEY

        followViewModel.getDataFollowing(username, token)

        followViewModel.isLoading2.observe(requireActivity()){
            if (it){
                binding.also {
                    it.shimmerFollowing.startShimmer()
                    it.shimmerFollowing.visibility = View.VISIBLE
                    it.rvFollowing.visibility = View.INVISIBLE
                }
            }else {
                binding.also {
                    it.shimmerFollowing.stopShimmer()
                    it.shimmerFollowing.visibility = View.INVISIBLE
                    it.rvFollowing.visibility = View.VISIBLE

                    followViewModel.following.observe(requireActivity()){ dataFollowing ->
                        showRecycleList(dataFollowing)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showRecycleList(list: List<DataFollowing>){
        val recycleGithub = binding.rvFollowing
        recycleGithub.layoutManager = LinearLayoutManager(requireContext())
        listDataFollowing = ListDataFollowing(list)
        recycleGithub.adapter = listDataFollowing
        listDataFollowing.setOnClickCallback(object : ListDataFollowing.OnItemClickCallback {
            override fun onItemClicked(data: DataFollowing, position: Int, viewAdapter: View) {
                Toast.makeText(requireContext(), "Anda menekan ${data.login}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}