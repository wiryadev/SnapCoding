package com.wiryadev.snapcoding.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.MainCoroutineRule
import com.wiryadev.snapcoding.data.remote.network.SnapCodingService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SnapRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: SnapCodingService
    private lateinit var repository: SnapRepository

    @Before
    fun setUp() {
        apiService = FakeApiService()
        repository = SnapRepository(apiService)
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
        val actualResult = apiService.register("Test","test@gmail.com", "12345678").body()!!
        actualResult shouldNotBe null
        actualResult shouldBeEqualTo expectedResult
        actualResult.message shouldBeEqualTo expectedResult.message
    }

}