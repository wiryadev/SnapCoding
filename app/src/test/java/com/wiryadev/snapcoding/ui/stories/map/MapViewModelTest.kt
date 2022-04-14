package com.wiryadev.snapcoding.ui.stories.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.SnapRepository
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: SnapRepository = mock()
    private val preference: UserPreference = mock()

    private lateinit var viewModel: MapViewModel

    private val successResult = DataDummy.generateSuccessStoriesResponse()
    private val successUiState = MapUiState(
        isLoading = false,
        errorMessages = null,
        stories = successResult.listStory
    )
    private val failedUiState = MapUiState(
        isLoading = false,
        errorMessages = "Error",
    )

    private val user = DataDummy.generateDummyUserSession()

    @Before
    fun setUp() {
        viewModel = MapViewModel(preference, repository)
    }

    @Test
    fun `when GetUser should Return Authenticated User`() = runTest {
        val expectedUser = MutableLiveData<UserSessionModel>()
        expectedUser.value = user

        whenever(preference.getUserSession())
            .doReturn(flowOf(user))

        val actualUser = viewModel.user.getOrAwaitValue()
        verify(preference).getUserSession()
        actualUser shouldBeEqualTo expectedUser.value
    }

    @Test
    fun `when GetStories for Map Should Return Success`() = runTest {
        val expectedUiState = MutableLiveData<MapUiState>()
        expectedUiState.value = successUiState

        whenever(repository.getStoriesForMap("Bearer token"))
            .doReturn(flowOf(Result.Success(successResult)))

        viewModel.getStoriesForMap("token")
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).getStoriesForMap("Bearer token")

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedUiState.value
        actualUiState.stories shouldBeEqualTo expectedUiState.value!!.stories
    }

    @Test
    fun `when GetStories for Map Should Return Error`() = runTest {
        val expectedUiState = MutableLiveData<MapUiState>()
        expectedUiState.value = failedUiState

        whenever(repository.getStoriesForMap("Bearer token"))
            .doReturn(flowOf(Result.Error("Error")))

        viewModel.getStoriesForMap("token")
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).getStoriesForMap("Bearer token")

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedUiState.value
        actualUiState.errorMessages shouldNotBe null
        actualUiState.errorMessages shouldBe "Error"
    }
}