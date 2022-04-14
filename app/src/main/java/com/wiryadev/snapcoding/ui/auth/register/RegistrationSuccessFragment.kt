package com.wiryadev.snapcoding.ui.auth.register

import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wiryadev.snapcoding.databinding.FragmentRegistrationSuccessBinding
import com.wiryadev.snapcoding.utils.DEFAULT_START_DELAY_DURATION
import com.wiryadev.snapcoding.utils.animateAlphaToVisible
import com.wiryadev.snapcoding.utils.animateBannerTranslationX

class RegistrationSuccessFragment : Fragment() {

    private var _binding: FragmentRegistrationSuccessBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationSuccessBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        playAnimation()

        binding?.btnLogin?.setOnClickListener {
            findNavController().navigate(
                RegistrationSuccessFragmentDirections.actionRegistrationSuccessFragmentToLoginFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding?.run {
            tvTitle.alpha = 0f
            tvSubtitle.alpha = 0f
        }
    }

    private fun playAnimation() {
        binding?.run {
            imageView.animateBannerTranslationX().start()

            AnimatorSet().apply {
                playSequentially(
                    tvTitle.animateAlphaToVisible(),
                    tvSubtitle.animateAlphaToVisible(),
                    btnLogin.animateAlphaToVisible(),
                )
                startDelay = DEFAULT_START_DELAY_DURATION
            }.start()
        }
    }
}