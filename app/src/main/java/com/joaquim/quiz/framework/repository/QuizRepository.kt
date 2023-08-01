package com.joaquim.quiz.framework.repository

import com.joaquim.quiz.framework.data.local.QuizDao
import com.joaquim.quiz.framework.data.model.question.QuestionModel
import com.joaquim.quiz.framework.data.model.user.UserModel
import com.joaquim.quiz.framework.data.remote.QuizApi
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val api: QuizApi,
    private val dao: QuizDao
) {
    suspend fun getUser() = api.getQuestion()

    suspend fun insert(userModel: UserModel) = dao.insert(userModel)
    fun getAll() = dao.getAll()
    suspend fun delete(userModel: UserModel) = dao.delete(userModel)
}