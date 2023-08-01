package com.joaquim.quiz.framework.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joaquim.quiz.framework.data.model.user.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userModel: UserModel): Long

    @Query("SELECT * FROM userModel ORDER BY id")
    fun getAll(): Flow<UserModel>

    @Delete
    suspend fun delete(user: UserModel)
}