package com.wiryadev.snapcoding.ui.auth.register

import android.animation.AnimatorSet
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.wiryadev.snapcoding.databinding.FragmentRegisterBinding
import com.wiryadev.snapcoding.utils.START_DELAY_DURATION
import com.wiryadev.snapcoding.utils.animateAlpha
import com.wiryadev.snapcoding.utils.animateBannerTranslationX

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

    private fun playAnimation() {
        // Banner Image Animation
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
                startDelay = START_DELAY_DURATION
            }.start()
        }
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

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}