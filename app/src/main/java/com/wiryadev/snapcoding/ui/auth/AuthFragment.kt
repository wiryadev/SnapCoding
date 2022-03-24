package com.wiryadev.snapcoding.ui.auth

import android.animation.AnimatorSet
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wiryadev.snapcoding.databinding.FragmentAuthBinding
import com.wiryadev.snapcoding.utils.DEFAULT_START_DELAY_DURATION
import com.wiryadev.snapcoding.utils.animateAlphaToVisible
import com.wiryadev.snapcoding.utils.animateBannerTranslationX

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        playAnimation()

        binding?.run {
            btnLogin.setOnClickListener {
                findNavController().navigate(
                    AuthFragmentDirections.actionAuthFragmentToLoginFragment()
                )
            }

            btnRegister.setOnClickListener {
                findNavController().navigate(
                    AuthFragmentDirections.actionAuthFragmentToRegisterFragment()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding?.run {
            btnLogin.alpha = 0f
            btnRegister.alpha = 0f
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
                    btnRegister.animateAlphaToVisible(),
                )
                startDelay = DEFAULT_START_DELAY_DURATION
            }.start()
        }
    }

}