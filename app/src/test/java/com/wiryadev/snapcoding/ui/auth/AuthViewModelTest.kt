package com.wiryadev.snapcoding.ui.auth

import app.cash.turbine.test
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.repository.auth.AuthRepository
import com.wiryadev.snapcoding.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var repository: AuthRepository

    private val loggedInUser = DataDummy.generateDummyUser()

    private val emptyUser = User("", "", "")

    @Test
    fun `when called, initial value should Return Loading`() = runTest {
        whenever(repository.getLoggedInUser())
            .doReturn(flowOf())
        val viewModel = AuthViewModel(repository)

        viewModel.uiState.test {
            val actual = awaitItem()
            actual shouldNotBe null
            actual shouldBeEqualTo AuthUiState.Loading
        }
    }

    @Test
    fun `when Logged In, isLogged in should be true`() = runTest {
        whenever(repository.getLoggedInUser())
            .doReturn(flowOf(Result.Success(loggedInUser)))
        val viewModel = AuthViewModel(repository)

        viewModel.uiState.test {
            val actual = awaitItem()
            actual shouldNotBe null
            (actual is AuthUiState.Loaded).shouldBeTrue()
            actual.isLoggedIn.shouldBeTrue()
        }
    }

    @Test
    fun `when Not Logged In, isLogged in should be false`() = runTest {
        whenever(repository.getLoggedInUser())
            .doReturn(flowOf(Result.Success(emptyUser)))
        val viewModel = AuthViewModel(repository)

        viewModel.uiState.test {
            val actual = awaitItem()
            actual shouldNotBe null
            (actual is AuthUiState.Loaded).shouldBeTrue()
            actual.isLoggedIn.shouldBeFalse()
        }
    }
}