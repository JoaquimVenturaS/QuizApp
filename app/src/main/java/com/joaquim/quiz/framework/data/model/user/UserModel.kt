package com.joaquim.quiz.framework.data.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "userModel")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("points")
    val points: Int = 0
)
