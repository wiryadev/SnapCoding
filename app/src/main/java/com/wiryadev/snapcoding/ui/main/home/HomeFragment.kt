package com.wiryadev.snapcoding.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.dataStore
import com.wiryadev.snapcoding.databinding.FragmentHomeBinding
import com.wiryadev.snapcoding.ui.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
    }

    private val storyAdapter by lazy {
        StoryAdapter { story, binding ->
            binding.tvName.transitionName = getString(R.string.transition_photo)
            binding.ivPhoto.transitionName = getString(R.string.transition_name)

            val extras = FragmentNavigatorExtras(
                binding.ivPhoto to getString(R.string.transition_photo),
                binding.tvName to getString(R.string.transition_name),
            )
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

        binding?.rvStories?.run {
            adapter = storyAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            uiState.token?.let {
                viewModel.getAllStories(it)
            }

            binding?.progressBar?.isVisible = uiState.isLoading

            if (uiState.stories.isNotEmpty()) {
                storyAdapter.setStories(uiState.stories)

                (view.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}