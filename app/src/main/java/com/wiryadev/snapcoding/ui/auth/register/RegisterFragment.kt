package com.wiryadev.snapcoding.ui.auth.register

import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wiryadev.snapcoding.databinding.FragmentRegisterBinding
import com.wiryadev.snapcoding.utils.DEFAULT_START_DELAY_DURATION
import com.wiryadev.snapcoding.utils.animateAlpha
import com.wiryadev.snapcoding.utils.animateBannerTranslationX
import com.wiryadev.snapcoding.utils.hideStatusBar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
            btnRegister.alpha = 0f
            tvTitle.alpha = 0f
            tvSubtitle.alpha = 0f
            tvName.alpha = 0f
            tvEmail.alpha = 0f
            tvPassword.alpha = 0f
            tilName.alpha = 0f
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
                    tvName.animateAlpha(),
                    tilName.animateAlpha(),
                    tvEmail.animateAlpha(),
                    tilEmail.animateAlpha(),
                    tvPassword.animateAlpha(),
                    tilPassword.animateAlpha(),
                    btnRegister.animateAlpha(),
                )
                startDelay = DEFAULT_START_DELAY_DURATION
            }.start()
        }
    }

}