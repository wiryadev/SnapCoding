package com.wiryadev.snapcoding.ui.main.detail

import android.animation.AnimatorSet
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import coil.load
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.databinding.FragmentDetailBinding
import com.wiryadev.snapcoding.utils.DEFAULT_START_DELAY_DURATION
import com.wiryadev.snapcoding.utils.animateAlphaToVisible
import com.wiryadev.snapcoding.utils.formatDate

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    private val detailArgs by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        sharedElementEnterTransition = ChangeBounds()

        val story = detailArgs.story

        binding?.run {
            ivStory.load(story.photoUrl)
            tvName.text = story.name
            tvDesc.text = story.description
            tvDate.text = getString(R.string.post_date, story.createdAt.formatDate())
        }
        playAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding?.run {
            tvDate.alpha = 0f
            tvDesc.alpha = 0f
        }
    }

    private fun playAnimation() {
        binding?.run {
            AnimatorSet().apply {
                playSequentially(
                    tvDate.animateAlphaToVisible(),
                    tvDesc.animateAlphaToVisible(),
                )
                startDelay = DEFAULT_START_DELAY_DURATION
            }.start()
        }
    }

}