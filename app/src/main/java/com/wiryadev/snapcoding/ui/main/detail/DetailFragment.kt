package com.wiryadev.snapcoding.ui.main.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.databinding.FragmentDetailBinding
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

        val story = detailArgs.story

        binding?.run {
            ivStory.load(story.photoUrl)
            tvName.text = story.name
            tvDesc.text = story.description
            tvDate.text = getString(R.string.post_date, story.createdAt.formatDate())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}