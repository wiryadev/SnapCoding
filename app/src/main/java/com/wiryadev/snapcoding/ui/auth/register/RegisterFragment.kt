package com.wiryadev.snapcoding.ui.auth.register

import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wiryadev.snapcoding.databinding.FragmentRegisterBinding
import com.wiryadev.snapcoding.utils.DEFAULT_START_DELAY_DURATION
import com.wiryadev.snapcoding.utils.animateAlpha
import com.wiryadev.snapcoding.utils.animateBannerTranslationX
import com.wiryadev.snapcoding.utils.showSnackbar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<RegisterViewModel>()

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

        if (!viewModel.animationTriggered) {
            setupView()
            playAnimation()
            viewModel.animationTriggered = true
        }
        initListener()

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding?.run {
                // Check loading and error
                btnRegister.isEnabled = !uiState.isLoading
                uiState.errorMessages?.let { error ->
                    root.showSnackbar(error)
                }

                /**
                 * if error does not exist and loading is finished
                 * it means registration success
                 * show success notification and move to login page
                 *
                 * TODO: move success notif to strings.xml and navigate to login
                 */
                if (!uiState.isLoading && uiState.errorMessages.isNullOrEmpty()) {
                    root.showSnackbar("Registration Success")
                }
            }
        }
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

    private fun initListener() {
        binding?.run {
            btnRegister.setOnClickListener {

                when {
                    etName.text.isNullOrEmpty() -> {
                        root.showSnackbar("Error Nama")
                    }
                    etEmail.text.isNullOrEmpty() || tilEmail.error != null -> {
                        root.showSnackbar("Error email")
                    }
                    etPassword.text.isNullOrEmpty() || tilPassword.error != null -> {
                        root.showSnackbar("Error password")
                    }
                    else -> {
                        val name = etName.text.toString()
                        val email = etEmail.text.toString()
                        val password = etPassword.text.toString()

                        viewModel.registerUser(
                            name, email, password
                        )
                    }
                }
            }
        }
    }

}