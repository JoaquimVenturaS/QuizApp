package com.joaquim.quiz.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.joaquim.quiz.R
import com.joaquim.quiz.databinding.FragmentLoginBinding
import com.joaquim.quiz.framework.data.model.user.UserModel
import com.joaquim.quiz.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val viewModel: LoginViewModel by viewModels()

    private lateinit var userModel: UserModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClick()
    }

    private fun setOnClick() {
        binding.login.setOnClickListener { login() }
    }

    private fun login() {
        userModel = UserModel(0, binding.username.text.toString())
        viewModel.login(userModel)
        updateUiWithUser()
        val action = LoginFragmentDirections.actionLoginFragmentToNavigationHome()
        findNavController().navigate(action)
    }

    private fun updateUiWithUser() {
        val welcome = getString(R.string.welcome)
        val displayName = binding.username.text.toString()
        Toast.makeText(
            requireContext(),
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_SHORT).show()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
}