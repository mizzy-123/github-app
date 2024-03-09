package com.dicoding.githubapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubapp.api.response.DataSearch
import com.dicoding.githubapp.api.response.DataSearchItems
import com.dicoding.githubapp.databinding.ItemListviewBinding

class ListDataSearchAdapter(val dataSearch: ArrayList<DataSearchItems>):
    RecyclerView.Adapter<ListDataSearchAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

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
        return dataSearch.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listSearch = dataSearch[position]
        Glide.with(holder.itemView.context)
            .load(listSearch.avatar_url)
            .into(holder.binding.avatar)
        holder.binding.name.text = listSearch.login
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSearch, position, it)
        }
    }
}