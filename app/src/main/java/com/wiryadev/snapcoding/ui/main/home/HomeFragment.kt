package com.wiryadev.snapcoding.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.dataStore
import com.wiryadev.snapcoding.databinding.FragmentHomeBinding
import com.wiryadev.snapcoding.ui.ViewModelFactory
import com.wiryadev.snapcoding.utils.showSnackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
    }

    private val storyAdapter by lazy {
        StoryAdapter { story ->
            binding?.root?.showSnackbar(story.name)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvStories?.run {
            adapter = storyAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            uiState.user?.let {
                viewModel.getAllStories(it.token)
            }

            binding?.progressBar?.isVisible = uiState.isLoading

            if (uiState.stories.isNotEmpty()) {
                storyAdapter.setStories(uiState.stories)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}