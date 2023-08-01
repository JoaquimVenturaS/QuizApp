package com.joaquim.quiz.framework.data.remote

import com.joaquim.quiz.framework.data.model.question.QuestionModelResponse
import retrofit2.Response
import retrofit2.http.GET

interface QuizApi {

    @GET("question")
    suspend fun getQuestion(): Response<QuestionModelResponse>

}