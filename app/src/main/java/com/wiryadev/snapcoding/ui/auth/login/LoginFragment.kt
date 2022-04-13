package com.wiryadev.snapcoding.ui.auth.login

import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.preference.user.dataStore
import com.wiryadev.snapcoding.databinding.FragmentLoginBinding
import com.wiryadev.snapcoding.ui.ViewModelFactory
import com.wiryadev.snapcoding.ui.stories.MainActivity
import com.wiryadev.snapcoding.utils.*

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory(
            UserPreference.getInstance(requireContext().dataStore),
            requireContext()
        )
    }

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

        if (!viewModel.animationTriggered) {
            setupView()
            playAnimation()
            viewModel.animationTriggered = true
        }

        initListener()

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding?.run {
                showLoading(uiState.isLoading)

                uiState.errorMessages?.let { error ->
                    root.showSnackbar(error)
                }

                uiState.loginResult?.let { result ->
                    viewModel.saveUser(
                        UserSessionModel(
                            name = result.name,
                            token = result.token,
                            isLoggedIn = true,
                        )
                    )
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
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
            btnLogin.alpha = 0f
            btnRegister.alpha = 0f
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
                    tvTitle.animateAlphaToVisible(),
                    tvSubtitle.animateAlphaToVisible(),
                    tvEmail.animateAlphaToVisible(),
                    tilEmail.animateAlphaToVisible(),
                    tvPassword.animateAlphaToVisible(),
                    tilPassword.animateAlphaToVisible(),
                    btnLogin.animateAlphaToVisible(),
                    btnRegister.animateAlphaToVisible(),
                )
                startDelay = DEFAULT_START_DELAY_DURATION
            }.start()
        }
    }

    private fun initListener() {
        binding?.run {
            btnLogin.setOnClickListener {
                when {
                    etEmail.text.isNullOrEmpty() || tilEmail.error != null -> {
                        tilEmail.error = getString(R.string.error_email_empty)
                    }
                    etPassword.text.isNullOrEmpty() || tilPassword.error != null -> {
                        tilPassword.error = getString(R.string.error_password_empty)
                    }
                    else -> {
                        val email = etEmail.text.toString()
                        val password = etPassword.text.toString()

                        viewModel.login(
                            email = email,
                            password = password,
                        )
                    }
                }
            }
            btnRegister.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                )
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.run {
            btnRegister.isVisible = !isLoading
            animateProgressAndButton(
                isLoading = isLoading,
                button = btnLogin,
                progressBar = progressBar,
            )
        }
    }

}