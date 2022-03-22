package com.wiryadev.snapcoding.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.databinding.FragmentAuthBinding

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
}