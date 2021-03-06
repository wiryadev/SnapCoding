package com.wiryadev.snapcoding.ui.stories.upload

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.SnapRepository
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
class UploadViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository: SnapRepository = mock()
    private val preference: UserPreference = mock()

    private lateinit var viewModel: UploadViewModel

    private lateinit var image: MultipartBody.Part
    private lateinit var description: RequestBody
    private lateinit var requestImageFile: RequestBody

    private val successResponse = DataDummy.generateSuccessUploadResponse()
    private val successUiState = UploadUiState(
        isLoading = false,
        errorMessages = null,
    )
    private val failedUiState = UploadUiState(
        isLoading = false,
        errorMessages = "Error",
    )

    @Before
    fun setUp() {
        viewModel = UploadViewModel(preference, repository)

        // Fake file for POST request
        description = "description".toRequestBody("text/plain".toMediaType())
        requestImageFile = "File".toRequestBody("image/jpeg".toMediaTypeOrNull())
        image = MultipartBody.Part.createFormData(
            "photo",
            "fileName",
            requestImageFile
        )
    }

    @Test
    fun `when Upload Should Return Success`() = runTest {
        val expectedUiState = MutableLiveData<UploadUiState>()
        expectedUiState.value = successUiState

        whenever(repository.upload("Bearer token", image, description))
            .doReturn(flowOf(Result.Success(successResponse)))

        viewModel.upload("token", image, description)
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).upload("Bearer token", image, description)

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedUiState.value
    }

    @Test
    fun `when Upload Should Return Error`() = runTest {
        val expectedUiState = MutableLiveData<UploadUiState>()
        expectedUiState.value = failedUiState

        whenever(repository.upload("Bearer token", image, description))
            .doReturn(flowOf(Result.Error("Error")))

        viewModel.upload("token", image, description)
        val actualUiState = viewModel.uiState.getOrAwaitValue()
        verify(repository).upload("Bearer token", image, description)

        actualUiState shouldNotBe null
        actualUiState shouldBeEqualTo expectedUiState.value
    }
}