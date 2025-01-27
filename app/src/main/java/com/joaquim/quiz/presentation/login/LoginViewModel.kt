package com.joaquim.quiz.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaquim.quiz.framework.data.model.user.UserModel
import com.joaquim.quiz.framework.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    fun login(userModel: UserModel) = viewModelScope.launch {
        repository.loadUser(userModel.name).collect { user ->
            if (user != null) {
                insert(user)
            } else {
                insert(userModel)
            }
        }
    }

    fun insert(userModel: UserModel) = viewModelScope.launch {
        repository.insert(userModel)
    }

}
