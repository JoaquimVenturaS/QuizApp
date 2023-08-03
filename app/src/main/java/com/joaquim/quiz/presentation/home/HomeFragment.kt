package com.joaquim.quiz.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaquim.quiz.databinding.FragmentHomeBinding
import com.joaquim.quiz.framework.util.toast
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
        collectAnswerObserver()
        observeQuestionsResolved()
    }

    private fun collectQuestionObserver() = lifecycleScope.launch {
        viewModel.details.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let { values ->
                        binding.progressCircular.visibility = View.GONE
                        optionsAdapter.options = values.options
                        binding.tvQuestion.text = values.statement
                        setupRecycleView()
                    }
                }

                is ResourceState.Error -> {
                    binding.progressCircular.visibility = View.GONE
                    resource.message?.let {
                        toast("Ocorreu um erro")
                    }
                }

                is ResourceState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                else -> {
                }
            }
        }
    }

    private fun collectAnswerObserver() = lifecycleScope.launch {
        viewModel.response.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    resource.data?.let { value ->
                        //observeQuestionsResolved()
                        viewModel.incremeantQuestions()
                        if (value.result) {
                            toast("Acertou!")
                            viewModel.incremeantCorrectQuestions()
                        } else toast("Errou :(")
                        viewModel.fetch()
                    }
                }

                is ResourceState.Error -> {
                    binding.progressCircular.visibility = View.GONE
                    toast("Falha na requisição")
                }

                is ResourceState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                else -> {
                }
            }
        }
    }

    private fun observeQuestionsResolved()  {
        viewModel.counterQuestionsResolved.observe(viewLifecycleOwner) {
            if (it >= 1) {
                val action =
                    HomeFragmentDirections.actionNavigationHomeToNavigationDashboard(
                        viewModel.counterCorrectQuestions.value
                    )
                findNavController().navigate(action)
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
            viewModel.details.value.let {
                viewModel.sendAnswer(optionModel, it.data?.id?.toInt()!!)
            }
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