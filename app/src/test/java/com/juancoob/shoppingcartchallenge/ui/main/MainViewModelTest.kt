package com.juancoob.shoppingcartchallenge.ui.main

import app.cash.turbine.test
import com.juancoob.shoppingcartchallenge.testRules.CoroutineTestRule
import com.juancoob.testshared.mockedDorm
import com.juancoob.usecases.GetAvailableDormsUseCase
import com.juancoob.usecases.GetStoredDormsUseCase
import com.juancoob.usecases.InsertDormsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var insertDormUseCase: InsertDormsUseCase

    @RelaxedMockK
    lateinit var getAvailableDormsUseCase: GetAvailableDormsUseCase

    @RelaxedMockK
    lateinit var getStoredDormsUseCase: GetStoredDormsUseCase

    private lateinit var mainViewModel: MainViewModel

    private val mockedDormList = listOf(
        mockedDorm,
        mockedDorm.copy(
            id = 1,
            type = "8-bed dorm",
            bedsAvailable = 8,
            pricePerBed = 14.50,
        ),
        mockedDorm.copy(
            id = 2,
            type = "12-bed dorm",
            bedsAvailable = 12,
            pricePerBed = 12.01,
        )
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mainViewModel =
            MainViewModel(insertDormUseCase, getAvailableDormsUseCase, getStoredDormsUseCase)
    }

    @Test
    fun `Progress is shown when the app inserts dorms and hidden when it finishes`() =
        runTest {
            mainViewModel.state.test {
                val mockedDormsFlow = flowOf(mockedDormList)
                coEvery { getStoredDormsUseCase().isEmpty() }.returns(true)
                coEvery { insertDormUseCase(any()) }.returns(null)
                coEvery { getAvailableDormsUseCase() }.returns(mockedDormsFlow)
                assertEquals(MainViewModel.UiState(), awaitItem())
                assertEquals(MainViewModel.UiState(loading = true), awaitItem())
                assertEquals(
                    MainViewModel.UiState(
                        loading = false,
                        dorms = mockedDormList,
                        errorRetrieved = null
                    ), awaitItem()
                )
                cancel()
            }
        }

    @Test
    fun `Progress is shown when the app loads dorms and hidden when it finishes`() = runTest {
        mainViewModel.state.test {
            val mockedDormsFlow = flowOf(listOf(mockedDorm))
            coEvery { getStoredDormsUseCase().isEmpty() }.returns(false)
            coEvery { getAvailableDormsUseCase() }.returns(mockedDormsFlow)
            assertEquals(MainViewModel.UiState(), awaitItem())
            assertEquals(MainViewModel.UiState(loading = true), awaitItem())
            assertEquals(
                MainViewModel.UiState(
                    loading = false,
                    dorms = listOf(mockedDorm),
                    errorRetrieved = null
                ), awaitItem()
            )
            cancel()
        }
    }
}
