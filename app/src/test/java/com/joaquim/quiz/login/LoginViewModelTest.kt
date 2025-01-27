package com.joaquim.quiz.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.joaquim.quiz.framework.data.model.user.UserModel
import com.joaquim.quiz.framework.repository.QuizRepository
import com.joaquim.quiz.presentation.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    @Mock
    lateinit var repository: QuizRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `test login when user does not exist`() = runTest {
        val userModel = UserModel(0, "TestUser", 0)

        `when`(repository.loadUser(userModel.name)).thenReturn(flowOf(null))

        viewModel.login(userModel)
        verify(repository).insert(userModel)
    }

    @Test
    fun `test login when user exists`() = runTest {
        val userModel = UserModel(0, "TestUser", 0)
        val existingUser = UserModel(0, "TestUser", 1)

        `when`(repository.loadUser(userModel.name)).thenReturn(flowOf(existingUser))

        viewModel.login(userModel)
        verify(repository).insert(existingUser)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
