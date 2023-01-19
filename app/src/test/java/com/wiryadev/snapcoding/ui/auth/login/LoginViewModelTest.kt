package com.wiryadev.snapcoding.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.repository.auth.AuthRepository
import com.wiryadev.snapcoding.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: AuthRepository

    private lateinit var viewModel: LoginViewModel

    private val user = DataDummy.generateDummyUser()
    private val errorMessage = "Unknown Error"
    private val expectedSuccessState = LoginUiState(
        loginResult = user,
        isLoading = false,
        errorMessages = null,
    )
    private val expectedFailedState = LoginUiState(
        loginResult = null,
        isLoading = false,
        errorMessages = errorMessage,
    )

    @Before
    fun setUp() {
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `when SaveUser verify Preference Invoked`() = runTest {
        whenever(repository.saveUserSession(user))
            .doReturn(flowOf(Result.Success(Unit)))
        viewModel.saveUser(user)
        verify(repository, atLeastOnce()).saveUserSession(user)
    }

    @Test
    fun `when Login Should Return Success`() = runTest {
        whenever(repository.login("test@gmail.com", "dummyPassword"))
            .doReturn(flowOf(Result.Success(user)))

        viewModel.login("test@gmail.com", "dummyPassword")
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).login("test@gmail.com", "dummyPassword")

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedSuccessState
        actualUiState.loginResult shouldBeEqualTo expectedSuccessState.loginResult
    }

    @Test
    fun `when Login Should Return Error`() = runTest {
        whenever(repository.login("test@gmail.com", "dummyPassword"))
            .doReturn(flowOf(Result.Error(errorMessage)))

        viewModel.login("test@gmail.com", "dummyPassword")
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).login("test@gmail.com", "dummyPassword")

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedFailedState
        actualUiState.loginResult shouldBe null
        actualUiState.errorMessages shouldBe errorMessage
    }

}