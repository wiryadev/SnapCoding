package com.wiryadev.snapcoding.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val user = DataDummy.generateDummyUserSession()

    private val viewModel: SplashViewModel = mock()

    @Test
    fun `when GetUser should Return Authenticated User`() {
        val expectedUser = MutableLiveData<UserSessionModel>()
        expectedUser.value = user

        whenever(viewModel.getUser())
            .doReturn(expectedUser)

        val actualUser = viewModel.getUser().getOrAwaitValue()
        actualUser shouldBeEqualTo expectedUser.value
    }

}