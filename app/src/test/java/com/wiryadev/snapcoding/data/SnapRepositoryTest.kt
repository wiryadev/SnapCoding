package com.wiryadev.snapcoding.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.local.SnapDatabase
import com.wiryadev.snapcoding.data.remote.network.SnapCodingService
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.Mock

@ExperimentalCoroutinesApi
class SnapRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: SnapCodingService
    private lateinit var database: SnapDatabase
    private lateinit var repository: SnapRepository

    private lateinit var image: MultipartBody.Part
    private lateinit var description: RequestBody
    private lateinit var requestImageFile: RequestBody

    @Before
    fun setUp() {
        apiService = FakeApiService()
        database = mockk()
        repository = SnapRepository(apiService, database)

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
    fun `when Login Should Return Success`() = runTest {
        val expectedResult = DataDummy.generateSuccessLoginResponse()
        val actualResult = apiService.login("test@gmail.com", "12345678").body()!!
        actualResult shouldNotBe null
        actualResult shouldBeEqualTo expectedResult
        actualResult.loginResult shouldBeEqualTo expectedResult.loginResult
    }

    @Test
    fun `when Register Should Return Success`() = runTest {
        val expectedResult = DataDummy.generateSuccessRegisterResponse()
        val actualResult = apiService.register("Test", "test@gmail.com", "12345678").body()!!
        actualResult shouldNotBe null
        actualResult shouldBeEqualTo expectedResult
        actualResult.message shouldBeEqualTo expectedResult.message
    }

    @Test
    fun `when Upload Should Return Success`() = runTest {
        val expectedResult = DataDummy.generateSuccessUploadResponse()
        val actualResult = apiService.uploadImage("token", image, description).body()!!
        actualResult shouldNotBe null
        actualResult shouldBeEqualTo expectedResult
        actualResult.message shouldBeEqualTo expectedResult.message
    }

}