package com.joaquim.quiz.presentation.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaquim.quiz.R
import com.joaquim.quiz.databinding.FragmentHomeBinding
import com.joaquim.quiz.framework.util.toast
import com.joaquim.quiz.presentation.adapters.OptionsAdapter
import com.joaquim.quiz.presentation.state.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private val optionsAdapter by lazy { OptionsAdapter() }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetch()
        clickAdapter()
        collectQuestionObserver()
        collectAnswerObserver()
    }

    private fun collectQuestionObserver() = lifecycleScope.launch {
        viewModel.details.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let { values ->4
                        binding.rvOptions.visibility = View.VISIBLE
                        binding.progressCircular.visibility = View.GONE
                        optionsAdapter.options = values.options
                        binding.tvQuestion.text = values.statement
                        setupRecycleView()
                    }
                }

                is ResourceState.Error -> {
                    reloadOnError()
                }

                is ResourceState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                else -> {
                }
            }
        }
    }

    private fun reloadOnError() {
        binding.run {
            progressCircular.visibility = View.GONE
            rvOptions.visibility = View.GONE
            cstError.visibility = View.VISIBLE
            btReload.setOnClickListener {
                cstError.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
                viewModel.fetch()
            }
        }
    }

    private fun collectAnswerObserver() = lifecycleScope.launch {
        viewModel.response.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    resource.data?.let { value ->
                        onResultResponseQuestion(value.result)
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

    private fun onResultResponseQuestion(isCorrect: Boolean) = with(binding) {
        if (isCorrect) {
            tvQuestion.text = "Acertou!"
            tvQuestion.background =
                ContextCompat.getDrawable(requireContext(), R.color.green)
            viewModel.incrementCorrectQuestions()
        } else {
            tvQuestion.text = "Errou :("
            tvQuestion.background =
                ContextCompat.getDrawable(requireContext(), R.color.red)
        }
        fetchQuestion()
    }

    private fun fetchQuestion() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvQuestion.background =
                ContextCompat.getDrawable(requireContext(), R.color.black)
            viewModel.incrementQuestions()
            if (viewModel.counterQuestionsResolved.value >= 10) {
                val action = HomeFragmentDirections
                    .actionNavigationHomeToNavigationFinishQuiz(viewModel.counterCorrectQuestions.value)
                view?.findNavController()?.navigate(action)
            } else {
                viewModel.fetch()
            }
        }, 2000)
    }

    private fun startAnimationLoader() = with(binding) {
        progressCircular.visibility = View.VISIBLE
        progressCircular.animate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}