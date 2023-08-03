package com.joaquim.quiz.framework.data.model.answer

import com.google.gson.annotations.SerializedName

data class AnswerModelRequest(
    @SerializedName("answer") val answer: String
)