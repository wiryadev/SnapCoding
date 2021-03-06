package com.wiryadev.snapcoding.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.SnapRepository
import com.wiryadev.snapcoding.data.preference.user.UserPreference
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
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: SnapRepository = mock()
    private val preference: UserPreference = mock()

    private lateinit var viewModel: LoginViewModel

    private val successLoginResult = DataDummy.generateSuccessLoginResult()
    private val successUiState = LoginUiState(
        loginResult = successLoginResult,
        isLoading = false,
        errorMessages = null,
    )
    private val failedUiState = LoginUiState(
        loginResult = null,
        isLoading = false,
        errorMessages = "Error",
    )

    private val user = DataDummy.generateDummyUserSession()

    @Before
    fun setUp() {
        viewModel = LoginViewModel(preference, repository)
    }

    @Test
    fun `when SaveUser verify Preference Invoked`() = runTest {
        viewModel.saveUser(user)
        verify(preference, atLeastOnce()).saveUserSession(user)
    }

    @Test
    fun `when Login Should Return Success`() = runTest {
        val expectedUiState = MutableLiveData<LoginUiState>()
        expectedUiState.value = successUiState

        whenever(repository.login("test@gmail.com", "dummyPassword"))
            .doReturn(flowOf(Result.Success(successLoginResult)))

        viewModel.login("test@gmail.com", "dummyPassword")
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).login("test@gmail.com", "dummyPassword")

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedUiState.value
        actualUiState.loginResult shouldBeEqualTo expectedUiState.value!!.loginResult
    }

    @Test
    fun `when Login Should Return Error`() = runTest {
        val expectedUiState = MutableLiveData<LoginUiState>()
        expectedUiState.value = failedUiState

        whenever(repository.login("test@gmail.com", "dummyPassword"))
            .doReturn(flowOf(Result.Error("Error")))

        viewModel.login("test@gmail.com", "dummyPassword")
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).login("test@gmail.com", "dummyPassword")

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedUiState.value
        actualUiState.loginResult shouldBe null
        actualUiState.errorMessages shouldBe "Error"
    }

}