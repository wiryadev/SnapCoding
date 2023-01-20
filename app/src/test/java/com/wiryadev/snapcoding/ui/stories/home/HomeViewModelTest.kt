package com.wiryadev.snapcoding.ui.stories.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.repository.story.StoryRepository
import com.wiryadev.snapcoding.getOrAwaitValue
import com.wiryadev.snapcoding.model.Story
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var repository: StoryRepository

    @Test
    fun `when GetStories Should Success`() = runTest {
        val dummyData = DataDummy.generateListStory()
        val dummyPagingData = PagedTestDataSources.snapshot(dummyData)
        val stories = MutableLiveData<PagingData<Story>>()
        stories.value = dummyPagingData

        whenever(repository.getStories()).doReturn(flowOf(dummyPagingData))
        val viewModel = HomeViewModel(repository)
        val actual = viewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        advanceUntilIdle()
        differ.submitData(actual)

        verify(repository).getStories()
        differ.snapshot() shouldNotBe null
        differ.snapshot().size shouldBeEqualTo dummyData.size
        differ.snapshot()[0]?.id shouldBeEqualTo dummyData[0].id
    }

    @Test
    fun `when GetStories Should Return Empty`() = runTest {
        val dummyPagingData = PagedTestDataSources.snapshot(emptyList())
        val stories = MutableLiveData<PagingData<Story>>()
        stories.value = dummyPagingData

        whenever(repository.getStories()).doReturn(flowOf(dummyPagingData))
        val viewModel = HomeViewModel(repository)
        val actual = viewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )
        advanceUntilIdle()
        differ.submitData(actual)

        verify(repository).getStories()
        differ.snapshot().isEmpty().shouldBeTrue()
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class PagedTestDataSources :
    PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}