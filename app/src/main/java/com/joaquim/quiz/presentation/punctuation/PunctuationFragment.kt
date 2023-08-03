package com.joaquim.quiz.presentation.punctuation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.joaquim.quiz.databinding.FragmentPunctuationBinding
import com.joaquim.quiz.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PunctuationFragment(
    override val viewModel: PunctuationViewModel
) : BaseFragment<FragmentPunctuationBinding, PunctuationViewModel>() {

    private val args: PunctuationFragmentArgs by navArgs()
    private var points: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        points = args.points
        binding.dialogMessageFlash.text = "Voce acertou $points de 10 questoes"
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPunctuationBinding =
        FragmentPunctuationBinding.inflate(inflater, container, false)
}