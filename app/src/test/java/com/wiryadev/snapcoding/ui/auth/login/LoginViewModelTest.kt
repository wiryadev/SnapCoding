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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: SnapRepository

    @Mock
    private lateinit var preference: UserPreference
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

    @Before
    fun setUp() {
        viewModel = LoginViewModel(preference, repository)
    }

    @Test
    fun `when Login Should Return Success`() = runTest {
        val expectedUiState = MutableLiveData<LoginUiState>()
        expectedUiState.value = successUiState

        `when`(repository.login("test@gmail.com", "dummyPassword")).thenReturn(
            flowOf(Result.Success(successLoginResult))
        )

        val actualUiState = viewModel.login("test@gmail.com", "dummyPassword").getOrAwaitValue()
        verify(repository).login("test@gmail.com", "dummyPassword")

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedUiState.value
        actualUiState.loginResult shouldBeEqualTo expectedUiState.value!!.loginResult
    }

    @Test
    fun `when Login Should Return Error`() = runTest {
        val expectedUiState = MutableLiveData<LoginUiState>()
        expectedUiState.value = failedUiState

        `when`(repository.login("test@gmail.com", "dummyPassword")).thenReturn(
            flowOf(Result.Error("Error"))
        )

        val actualUiState = viewModel.login("test@gmail.com", "dummyPassword").getOrAwaitValue()
        verify(repository).login("test@gmail.com", "dummyPassword")

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedUiState.value
        actualUiState.loginResult shouldBe null
        actualUiState.errorMessages shouldBe "Error"
    }

}