package com.wiryadev.snapcoding.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val preference: UserPreference = mock()

    private lateinit var viewModel: SettingsViewModel

    private val user = DataDummy.generateDummyUserSession()

    @Before
    fun setUp() {
        viewModel = SettingsViewModel(preference)
    }

    @Test
    fun `when SaveUser verify Preference Invoked`() = runTest {
        val expectedUser = MutableLiveData<UserSessionModel>()
        expectedUser.value = user

        whenever(preference.getUserSession())
            .doReturn(flowOf(user))

        val actual = viewModel.getUser().getOrAwaitValue()
        actual shouldNotBe null
        actual shouldBeEqualTo expectedUser.value
    }

    @Test
    fun `when LogOut verify Preference Invoked`() = runTest {
        viewModel.logout()
        verify(preference, atLeastOnce()).deleteUserSession()
    }
}