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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Assert.*

import org.junit.After
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

    @Before
    fun setUp() {
        viewModel = LoginViewModel(preference, repository)
    }

    @Test
    fun `when Login Should Return Success`() = runTest {
        val expectedUiState = MutableLiveData<LoginUiState>()
        expectedUiState.value = successUiState

        viewModel.login("test@gmail.com", "dummyPassword")
//        `when`(repository.login("test@gmail.com", "dummyPassword")).thenReturn(
//            flow { Result.Success(successLoginResult) }
//        )
        verify(repository).login("test@gmail.com", "dummyPassword")

        val actualUiState = viewModel.uiState.getOrAwaitValue()
        actualUiState.loginResult shouldBeEqualTo successLoginResult
    }
}