package com.joaquim.quiz.framework.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joaquim.quiz.framework.data.model.user.UserModel

@Database(entities = [UserModel::class], version = 1, exportSchema = false)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}