package com.joaquim.quiz.framework.data.model.question

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class QuestionModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("options")
    val options: List<String>,
    @SerializedName("statement")
    val statement: String
) : Serializable