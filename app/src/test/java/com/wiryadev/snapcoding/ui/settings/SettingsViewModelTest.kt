package com.wiryadev.snapcoding.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: AuthRepository

    private lateinit var viewModel: SettingsViewModel

    private val user = DataDummy.generateDummyUser()

    @Before
    fun setUp() {
        viewModel = SettingsViewModel(repository)
    }

    @Test
    fun `when SaveUser verify Preference Invoked`() = runTest {
        val expected = Result.Success(user)

        whenever(repository.getLoggedInUser())
            .doReturn(flowOf(expected))

        val actual = viewModel.getUser().getOrAwaitValue()
        actual shouldNotBe null
        actual shouldBeEqualTo expected
    }

    @Test
    fun `when LogOut verify Preference Invoked`() = runTest {
        whenever(repository.removeUserSession())
            .doReturn(flowOf(Result.Success(Unit)))
        viewModel.logout()
        verify(repository, atLeastOnce()).removeUserSession()
    }
}