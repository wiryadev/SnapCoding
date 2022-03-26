package com.wiryadev.snapcoding.ui.auth.register

import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.databinding.FragmentRegisterBinding
import com.wiryadev.snapcoding.utils.*

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
                showLoading(uiState.isLoading)

                uiState.errorMessages?.let { error ->
                    root.showSnackbar(error)
                }

                /**
                 * if error does not exist and loading is finished
                 * it means registration success
                 * show success notification and move to login page
                 */
                if (!uiState.isLoading && uiState.errorMessages.isNullOrEmpty()) {
                    findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToRegistrationSuccessFragment()
                    )
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
                    tvTitle.animateAlphaToVisible(),
                    tvSubtitle.animateAlphaToVisible(),
                    tvName.animateAlphaToVisible(),
                    tilName.animateAlphaToVisible(),
                    tvEmail.animateAlphaToVisible(),
                    tilEmail.animateAlphaToVisible(),
                    tvPassword.animateAlphaToVisible(),
                    tilPassword.animateAlphaToVisible(),
                    btnRegister.animateAlphaToVisible(),
                )
                startDelay = DEFAULT_START_DELAY_DURATION
            }.start()
        }
    }

    private fun initListener() {
        binding?.run {
            etName.doAfterTextChanged {
                if (it.toString().isNotBlank()) {
                    tilName.error = null
                }
            }

            btnRegister.setOnClickListener {
                when {
                    etName.text.isNullOrEmpty() -> {
                        tilName.error = getString(R.string.error_name_empty)
                    }
                    etEmail.text.isNullOrEmpty() || tilEmail.error != null -> {
                        tilEmail.error = getString(R.string.error_email_empty)
                    }
                    etPassword.text.isNullOrEmpty() || tilPassword.error != null -> {
                        tilPassword.error = getString(R.string.error_password_empty)
                    }
                    else -> {
                        val name = etName.text.toString()
                        val email = etEmail.text.toString()
                        val password = etPassword.text.toString()

                        viewModel.registerUser(
                            name = name,
                            email = email,
                            password = password,
                        )
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.run {
            animateProgressAndButton(
                isLoading = isLoading,
                button = btnRegister,
                progressBar = progressBar,
            )
        }
    }

}