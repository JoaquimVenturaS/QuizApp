package com.joaquim.quiz.framework.data.remote

import com.joaquim.quiz.framework.data.model.answer.AnswerModelRequest
import com.joaquim.quiz.framework.data.model.result.ResultModelResponse
import com.joaquim.quiz.framework.data.model.question.QuestionModelResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface QuizApi {

    @GET("question")
    suspend fun getQuestion(): Response<QuestionModelResponse>

    @POST("answer?")
    suspend fun sendAnswer(
        @Query("questionId") id: Int,
        @Body answer: AnswerModelRequest
    ): Response<ResultModelResponse>

}
