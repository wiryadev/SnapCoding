package com.wiryadev.snapcoding.ui.main.home

import androidx.recyclerview.widget.DiffUtil
import com.wiryadev.snapcoding.data.remote.response.Story

class StoryDiffCallback(
    private val oldStories: List<Story>,
    private val newStories: List<Story>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldStories.size

    override fun getNewListSize(): Int = newStories.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStories[oldItemPosition].id == newStories[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldStories[oldItemPosition]
        val newItem = newStories[newItemPosition]
        return oldItem.photoUrl == newItem.photoUrl && oldItem.createdAt == newItem.createdAt
    }
}