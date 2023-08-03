package com.joaquim.quiz.framework.data.model.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResultModelResponse(
    @SerializedName("result")
    val result: Boolean
) : Serializable