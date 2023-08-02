package com.joaquim.quiz.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        collectQuestionObserver()
    }

    private fun collectQuestionObserver() = lifecycleScope.launch {
        viewModel.details.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let { values ->
                        optionsAdapter.comics = values.options
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

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding =
        FragmentHomeBinding.inflate(inflater, container, false)
}