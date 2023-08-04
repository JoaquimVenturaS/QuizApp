package com.joaquim.quiz.presentation.finish

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.joaquim.quiz.databinding.FragmentFinishQuizBinding
import com.joaquim.quiz.presentation.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishQuizFragment() : Fragment() {
    private var _binding: FragmentFinishQuizBinding? = null
    private val binding get() = _binding!!

    val viewModel: FinishQuizViewModel by viewModels()
    val args: FinishQuizFragmentArgs by navArgs()

    private var points = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishQuizBinding.inflate(inflater, container, false)
        setUpUi()
        setListeners()
        onBackPressed()
        return binding.root
    }

    private fun setUpUi() {
        points = args.points
        binding.dialogMessageFlash.text = "Você acertou $points de 10 questões"
    }

    private fun setListeners() {
        binding.btFinish.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        val action = FinishQuizFragmentDirections
            .actionNavigationFinishQuizToLoginFragment()
        AlertDialog.Builder(requireContext()).setMessage("O que gostaria de fazer?")
            .setPositiveButton("Recomeçar") { _, _ -> findNavController().navigate(action) }
            .setNegativeButton("Sair") { _, _ -> requireActivity().finish() }
            .setCancelable(true)
            .show()
    }

    private fun onBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}