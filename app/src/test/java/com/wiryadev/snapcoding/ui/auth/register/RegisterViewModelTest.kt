package com.wiryadev.snapcoding.ui.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.repository.auth.AuthRepository
import com.wiryadev.snapcoding.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: AuthRepository

    private lateinit var viewModel: RegisterViewModel

    private val successResponse = DataDummy.generateSuccessCommonModel()
    private val expectedSuccessState = RegisterUiState(
        isLoading = false,
        errorMessages = null,
    )
    private val expectedFailedState = RegisterUiState(
        isLoading = false,
        errorMessages = "Error",
    )

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when Register Should Return Success`() = runTest {
        whenever(repository.register("name", "email", "password"))
            .doReturn(flowOf(Result.Success(successResponse)))

        viewModel.registerUser("name", "email", "password")
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).register("name", "email", "password")

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedSuccessState
    }

    @Test
    fun `when Register Should Return Error`() = runTest {
        val expectedUiState = MutableLiveData<RegisterUiState>()
        expectedUiState.value = expectedFailedState

        whenever(repository.register("name", "email", "password"))
            .doReturn(flowOf(Result.Error("Error")))

        viewModel.registerUser("name", "email", "password")
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).register("name", "email", "password")

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedUiState.value
    }

}