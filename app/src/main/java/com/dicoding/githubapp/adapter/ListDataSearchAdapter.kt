package com.dicoding.githubapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubapp.api.response.DataSearchItems
import com.dicoding.githubapp.databinding.ItemListviewBinding
import com.dicoding.githubapp.helper.DataSearchDiffCallback

class ListDataSearchAdapter :
    RecyclerView.Adapter<ListDataSearchAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listDataSearch = ArrayList<DataSearchItems>()
    
    fun setListDataSearch(listDataSearch: List<DataSearchItems>){
        val diffCallback = DataSearchDiffCallback(this.listDataSearch, listDataSearch)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listDataSearch.clear()
        this.listDataSearch.addAll(listDataSearch)
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataSearchItems, position: Int, viewAdapter: View)
    }

    inner class ListViewHolder(var binding: ItemListviewBinding): RecyclerView.ViewHolder(binding.root)

    internal fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listDataSearch.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listSearch = listDataSearch[position]
        Glide.with(holder.itemView.context)
            .load(listSearch.avatar_url)
            .into(holder.binding.avatar)
        holder.binding.name.text = listSearch.login
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSearch, position, it)
        }
    }
}