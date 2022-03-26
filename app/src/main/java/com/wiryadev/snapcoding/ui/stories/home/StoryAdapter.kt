package com.wiryadev.snapcoding.ui.stories.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.remote.response.Story
import com.wiryadev.snapcoding.databinding.ItemStoryBinding

class StoryAdapter(
    private inline val onStoryClick: (Story, Navigator.Extras) -> Unit,
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
                ivPhoto.load(story.photoUrl)
                tvName.text = story.name
                root.setOnClickListener {
                    val keyTransitionName = it.resources.getString(R.string.transition_photo)
                    val keyTransitionPhoto = it.resources.getString(R.string.transition_name)

                    tvName.transitionName = keyTransitionName
                    ivPhoto.transitionName = keyTransitionPhoto

                    val extras = FragmentNavigatorExtras(
                        ivPhoto to keyTransitionName,
                        tvName to keyTransitionPhoto,
                    )

                    onStoryClick.invoke(story, extras)
                }
            }
        }
    }

}