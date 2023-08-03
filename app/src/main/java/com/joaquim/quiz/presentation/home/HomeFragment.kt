package com.joaquim.quiz.presentation.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaquim.quiz.databinding.FragmentHomeBinding
import com.joaquim.quiz.presentation.adapters.OptionsAdapter
import com.joaquim.quiz.presentation.base.BaseFragment
import com.joaquim.quiz.presentation.state.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()

    private val optionsAdapter by lazy { OptionsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetch()
        clickAdapter()
        collectQuestionObserver()
    }

    private fun collectQuestionObserver() = lifecycleScope.launch {
        viewModel.details.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let { values ->
                        optionsAdapter.options = values.options
                        binding.tvQuestion.text = values.statement
                        setupRecycleView()
                    }
                }

                is ResourceState.Error -> {
//                    binding.progressCircular.hide()
//                    resource.message?.let { message ->
//                        toast(getString(R.string.an_error_occurred))
//                        Timber.tag("ListCharacterFragment").e("Error -> $message")
//                    }
                }

                is ResourceState.Loading -> {
//                    binding.progressCircular.show()
                }

                else -> {
                }
            }
        }
    }

    private fun setupRecycleView() = with(binding) {
        rvOptions.apply {
            adapter = optionsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun clickAdapter() {
        optionsAdapter.setOnClickListener { optionModel ->
            startAnimationLoader()
            Toast.makeText(
                requireContext(), optionModel,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startAnimationLoader() = with(binding) {
        progressCircular.visibility = View.VISIBLE
        progressCircular.animate()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding =
        FragmentHomeBinding.inflate(inflater, container, false)
}