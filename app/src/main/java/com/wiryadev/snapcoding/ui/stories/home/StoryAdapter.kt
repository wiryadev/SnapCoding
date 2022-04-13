package com.wiryadev.snapcoding.ui.stories.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.remote.response.Story
import com.wiryadev.snapcoding.databinding.ItemStoryBinding

class StoryAdapter(
    private inline val onStoryClick: (Story, Navigator.Extras) -> Unit,
) : PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        data?.let { holder.bind(it) }
    }

    inner class StoryViewHolder(
        private val binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            with(binding) {
                ivPhoto.load(story.photoUrl) {
                    placeholder(R.drawable.ic_image_alt)
                }
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

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}