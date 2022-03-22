package com.wiryadev.snapcoding.ui.auth.login

import android.animation.AnimatorSet
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wiryadev.snapcoding.databinding.FragmentLoginBinding
import com.wiryadev.snapcoding.utils.DEFAULT_START_DELAY_DURATION
import com.wiryadev.snapcoding.utils.animateAlpha
import com.wiryadev.snapcoding.utils.animateBannerTranslationX

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        playAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding?.run {
            btnLogin.alpha = 0f
            tvTitle.alpha = 0f
            tvSubtitle.alpha = 0f
            tvEmail.alpha = 0f
            tvPassword.alpha = 0f
            tilEmail.alpha = 0f
            tilPassword.alpha = 0f
        }
    }

    private fun playAnimation() {
        binding?.run {
            imageView.animateBannerTranslationX().start()

            AnimatorSet().apply {
                playSequentially(
                    tvTitle.animateAlpha(),
                    tvSubtitle.animateAlpha(),
                    tvEmail.animateAlpha(),
                    tilEmail.animateAlpha(),
                    tvPassword.animateAlpha(),
                    tilPassword.animateAlpha(),
                    btnLogin.animateAlpha(),
                )
                startDelay = DEFAULT_START_DELAY_DURATION
            }.start()
        }
    }

}