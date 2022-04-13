package com.wiryadev.snapcoding.ui.stories.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.dataStore
import com.wiryadev.snapcoding.databinding.FragmentHomeBinding
import com.wiryadev.snapcoding.ui.ViewModelFactory
import com.wiryadev.snapcoding.ui.stories.upload.UploadActivity
import com.wiryadev.snapcoding.utils.showSnackbar

@ExperimentalPagingApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory(UserPreference.getInstance(requireContext().dataStore), requireContext())
    }

    private val storyAdapter by lazy {
        StoryAdapter { story, extras ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                    story = story
                ),
                extras,
            )
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
        postponeEnterTransition()

        binding?.rvStories?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = storyAdapter
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                viewModel.setToken(user.token)
                Log.d("Token", "token: executed")
            }
        }

        getData()

        binding?.fabAddStory?.setOnClickListener {
            val intent = Intent(context, UploadActivity::class.java)
            startActivity(intent)
        }

        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }

//        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
//            uiState.token?.let {
//                viewModel.getAllStories(it)
//            }
//
//            showLoading(uiState.isLoading)
//
//            uiState.errorMessages?.let {
//                binding?.root?.showSnackbar(it)
//            }
//
//            if (uiState.stories.isNotEmpty()) {
//                storyAdapter.setStories(uiState.stories)
//
//                (view.parent as? ViewGroup)?.doOnPreDraw {
//                    startPostponedEnterTransition()
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        viewModel.stories.observe(viewLifecycleOwner) {
            storyAdapter.submitData(lifecycle, it)
        }

        storyAdapter.loadStateFlow.asLiveData().observe(viewLifecycleOwner) { loadState ->
            binding?.progressBar?.isVisible = loadState.source.refresh is LoadState.Loading

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                binding?.root?.showSnackbar(it.error.message.toString())
            }
        }
    }
}