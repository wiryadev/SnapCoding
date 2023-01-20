package com.wiryadev.snapcoding.ui.stories.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.repository.story.StoryRepository
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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: StoryRepository

    private lateinit var viewModel: MapViewModel

    private val stories = DataDummy.generateListStory()
    private val successUiState = MapUiState(
        isLoading = false,
        errorMessages = null,
        stories = stories
    )
    private val failedUiState = MapUiState(
        isLoading = false,
        errorMessages = "Error",
    )

    private val user = DataDummy.generateDummyUserSession()

    @Before
    fun setUp() {
        viewModel = MapViewModel(repository)
    }

    @Test
    fun `when GetStories for Map Should Return Success`() = runTest {
        val expectedUiState = MutableLiveData<MapUiState>()
        expectedUiState.value = successUiState

        whenever(repository.getStoriesWithLocation())
            .doReturn(flowOf(Result.Success(stories)))

        viewModel.getStoriesWithLocation()
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).getStoriesWithLocation()

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo successUiState
        actualUiState.stories shouldBeEqualTo successUiState.stories
    }

    @Test
    fun `when GetStories for Map Should Return Error`() = runTest {
        val expectedUiState = MutableLiveData<MapUiState>()
        expectedUiState.value = failedUiState

        whenever(repository.getStoriesWithLocation())
            .doReturn(flowOf(Result.Error("Error")))

        viewModel.getStoriesWithLocation()
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).getStoriesWithLocation()

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo failedUiState
        actualUiState.errorMessages shouldNotBe null
        actualUiState.errorMessages shouldBe "Error"
    }
}