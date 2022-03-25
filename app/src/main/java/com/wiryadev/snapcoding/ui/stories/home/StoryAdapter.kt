package com.wiryadev.snapcoding.ui.stories.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wiryadev.snapcoding.data.remote.response.Story
import com.wiryadev.snapcoding.databinding.ItemStoryBinding

class StoryAdapter(
    private inline val onStoryClick: (Story, ItemStoryBinding) -> Unit,
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private val stories = mutableListOf<Story>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size

    fun setStories(newStories: List<Story>) {
        val diffCallback = StoryDiffCallback(this.stories, newStories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.stories.clear()
        this.stories.addAll(newStories)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class StoryViewHolder(
        private val binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            with(binding) {
                Log.d("StoryAdapter", "bind: ${story.photoUrl}")
                ivPhoto.load(story.photoUrl)
                tvName.text = story.name
                root.setOnClickListener {
                    onStoryClick.invoke(story, binding)
                }
            }
        }
    }

}