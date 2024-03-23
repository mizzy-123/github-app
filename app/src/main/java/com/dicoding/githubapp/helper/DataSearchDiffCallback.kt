package com.dicoding.githubapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubapp.api.response.DataSearchItems

class DataSearchDiffCallback(
    private val oldList: List<DataSearchItems>,
    private val newList: List<DataSearchItems>,
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newList = newList[newItemPosition]
        val oldList = oldList[oldItemPosition]
        return oldList.login == newList.login
    }

}