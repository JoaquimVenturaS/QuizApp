package com.joaquim.quiz.presentantion.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaquim.quiz.framework.data.model.question.QuestionModelResponse
import com.joaquim.quiz.framework.repository.QuizRepository
import com.joaquim.quiz.presentantion.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text


    private val _details =
        MutableStateFlow<ResourceState<QuestionModelResponse>>(ResourceState.Loading())
    val details: StateFlow<ResourceState<QuestionModelResponse>> = _details

    fun fetch() = viewModelScope.launch {
        safeFetch()
    }

    private suspend fun safeFetch() {
        _details.value = ResourceState.Loading()
        try {
            val response = repository.getQuestion()
            _details.value = handleResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _details.value =
                    ResourceState.Error("Erro de rede ou conexão com a internet")

                else -> _details.value = ResourceState.Error("Erro na conversão")
            }
        }
    }

    private fun handleResponse(response: Response<QuestionModelResponse>):
            ResourceState<QuestionModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }

    fun setPoints(userId: Int, points: Int) = viewModelScope.launch {
        repository.setPoints(userId, points)
    }
}