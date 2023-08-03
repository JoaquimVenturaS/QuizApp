package com.joaquim.quiz.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaquim.quiz.framework.data.model.answer.AnswerModelRequest
import com.joaquim.quiz.framework.data.model.question.QuestionModelResponse
import com.joaquim.quiz.framework.data.model.result.ResultModelResponse
import com.joaquim.quiz.framework.repository.QuizRepository
import com.joaquim.quiz.presentation.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    val details = _details.asStateFlow()

    private val _responseQuiz =
        MutableStateFlow<ResourceState<ResultModelResponse>>(ResourceState.Loading())
    val response = _responseQuiz.asStateFlow()

    private val _counterCorrectQuestions = MutableStateFlow(0)
    val counterCorrectQuestions = _counterCorrectQuestions.asStateFlow()

    private val _counterQuestionsResolved = MutableLiveData(0)
    val counterQuestionsResolved: LiveData<Int>
        get() = _counterQuestionsResolved

    fun incremeantCorrectQuestions() {
        _counterCorrectQuestions.update { count -> count + 1 } // atomic, safe for concurrent use
    }

    fun incremeantQuestions() {
        _counterQuestionsResolved.postValue(_counterCorrectQuestions.value + 1) // atomic, safe for concurrent use
    }


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


    fun sendAnswer(answer: String, id: Int) = viewModelScope.launch {
        safeSendAnswer(answer, id)
    }

    private suspend fun safeSendAnswer(answer: String, id: Int) {
        _responseQuiz.value = ResourceState.Loading()
        try {
            val answerModelRequest = AnswerModelRequest(answer)
            val response = repository.sendAnswer(id, answerModelRequest)
            _responseQuiz.value = handleResponseAnswer(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _details.value =
                    ResourceState.Error("Erro de rede ou conexão com a internet")

                else -> _details.value = ResourceState.Error("Erro na conversão")
            }
        }
    }

    private fun handleResponseAnswer(response: Response<ResultModelResponse>):
            ResourceState<ResultModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
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