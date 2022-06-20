package com.juancoob.shoppingcartchallenge.ui.main

import app.cash.turbine.test
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.shoppingcartchallenge.testRules.CoroutineTestRule
import com.juancoob.shoppingcartchallenge.ui.detail.DetailViewModel
import com.juancoob.testshared.mockedBed
import com.juancoob.testshared.mockedDorm
import com.juancoob.usecases.GetAvailableDormByIdUseCase
import com.juancoob.usecases.InsertBedForCheckoutUseCase
import com.juancoob.usecases.UpdateDormUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var getAvailableDormByIdUseCase: GetAvailableDormByIdUseCase

    @RelaxedMockK
    lateinit var updateDormUseCase: UpdateDormUseCase

    @RelaxedMockK
    lateinit var insertBedForCheckoutUseCase: InsertBedForCheckoutUseCase

    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        detailViewModel =
            DetailViewModel(
                mockedDorm.id,
                getAvailableDormByIdUseCase,
                updateDormUseCase,
                insertBedForCheckoutUseCase
            )
    }

    @Test
    fun `The app loads a dorm when the user clicks on its dorm detail`() =
        runTest {
            detailViewModel.state.test {
                coEvery { getAvailableDormByIdUseCase(mockedDorm.id) }.returns(flowOf(mockedDorm))
                assertEquals(DetailViewModel.UiState(), awaitItem())
                assertEquals(DetailViewModel.UiState(dorm = mockedDorm), awaitItem())
                cancel()
            }
        }

    @Test
    fun `The app calls to book a bed when the user book one`() =
        runTest {
            val mockedDormMinusOneBed =
                mockedDorm.copy(bedsAvailable = mockedDorm.bedsAvailable - 1)
            coEvery { insertBedForCheckoutUseCase(mockedBed) }.returns(null)
            coEvery { updateDormUseCase(mockedDormMinusOneBed) }.returns(
                null
            )

            `The app loads a dorm when the user clicks on its dorm detail`()
            detailViewModel.addBookedBeds(1)
            runCurrent()

            coVerify { updateDormUseCase(mockedDormMinusOneBed) }
        }

    @Test
    fun `The app won't insert a bed when the insertion fails`() =
        runTest {
            detailViewModel.state.test {
                coEvery { insertBedForCheckoutUseCase(any()) }.returns(ErrorRetrieved.Connectivity)
                `The app loads a dorm when the user clicks on its dorm detail`()
                detailViewModel.addBookedBeds(1)

                assertEquals(DetailViewModel.UiState(), awaitItem())
                assertEquals(DetailViewModel.UiState(dorm = mockedDorm), awaitItem())
                assertEquals(
                    DetailViewModel.UiState(
                        dorm = mockedDorm,
                        errorRetrieved = ErrorRetrieved.Connectivity
                    ), awaitItem()
                )
                cancel()
            }
        }

    @Test
    fun `The app won't update a dorm when the update fails`() =
        runTest {
            detailViewModel.state.test {
                coEvery { insertBedForCheckoutUseCase(any()) }.returns(null)
                coEvery { updateDormUseCase(any()) }.returns(ErrorRetrieved.Connectivity)
                `The app loads a dorm when the user clicks on its dorm detail`()
                detailViewModel.addBookedBeds(1)

                assertEquals(DetailViewModel.UiState(), awaitItem())
                assertEquals(DetailViewModel.UiState(dorm = mockedDorm), awaitItem())
                assertEquals(
                    DetailViewModel.UiState(
                        dorm = mockedDorm,
                        errorRetrieved = ErrorRetrieved.Connectivity
                    ), awaitItem()
                )
                cancel()
            }
        }
}
