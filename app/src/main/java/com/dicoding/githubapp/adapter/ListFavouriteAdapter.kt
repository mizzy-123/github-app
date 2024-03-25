package com.dicoding.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubapp.database.Favourite
import com.dicoding.githubapp.databinding.ItemListviewBinding

class ListFavouriteAdapter(private val dataFavourite: List<Favourite>) :
    RecyclerView.Adapter<ListFavouriteAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: ListFavouriteAdapter.OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Favourite)
    }

    internal fun setOnClickCallback(onItemClickCallback: ListFavouriteAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(var binding: ItemListviewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataFavourite.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val favourtie = dataFavourite[position]

        Glide.with(holder.itemView.context)
            .load(favourtie.avatar_url)
            .into(holder.binding.avatar)
        holder.binding.name.text = favourtie.login
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(favourtie)
        }
    }
}