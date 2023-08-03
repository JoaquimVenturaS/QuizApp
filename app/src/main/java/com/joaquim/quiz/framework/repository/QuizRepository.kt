package com.joaquim.quiz.framework.repository

import com.joaquim.quiz.framework.data.local.QuizDao
import com.joaquim.quiz.framework.data.model.answer.AnswerModelRequest
import com.joaquim.quiz.framework.data.model.question.QuestionModel
import com.joaquim.quiz.framework.data.model.user.UserModel
import com.joaquim.quiz.framework.data.remote.QuizApi
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val api: QuizApi,
    private val dao: QuizDao
) {
    suspend fun getQuestion() = api.getQuestion()

    suspend fun sendAnswer(id: Int, answer: AnswerModelRequest) = api.sendAnswer(id, answer)

    suspend fun insert(userModel: UserModel) = dao.insert(userModel)
    fun getAll() = dao.getAll()
    suspend fun delete(userModel: UserModel) = dao.delete(userModel)

    suspend fun setPoints(id: Int, points: Int) = dao.setPoints(id, points)

    suspend fun loadUser(name: String) = dao.loadUser(name)
}