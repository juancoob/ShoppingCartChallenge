package com.juancoob.shoppingcartchallenge.ui.main

import app.cash.turbine.test
import arrow.core.right
import com.juancoob.shoppingcartchallenge.testRules.CoroutineTestRule
import com.juancoob.shoppingcartchallenge.ui.checkout.CheckoutViewModel
import com.juancoob.testshared.TO
import com.juancoob.testshared.mockedDorm
import com.juancoob.usecases.DeleteBedForCheckoutUseCase
import com.juancoob.usecases.GetCartUseCase
import com.juancoob.usecases.GetConversionUseCase
import com.juancoob.usecases.GetStoredDormsUseCase
import com.juancoob.usecases.GetSymbolsUseCase
import com.juancoob.usecases.InsertBedForCheckoutUseCase
import com.juancoob.usecases.RequestSymbolsUseCase
import com.juancoob.usecases.UpdateBedsUseCase
import com.juancoob.usecases.UpdateDormUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
class CheckoutViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var requestSymbolsUseCase: RequestSymbolsUseCase

    @RelaxedMockK
    lateinit var getSymbolsUseCase: GetSymbolsUseCase

    @RelaxedMockK
    lateinit var getCartUseCase: GetCartUseCase

    @RelaxedMockK
    lateinit var insertBedForCheckoutUseCase: InsertBedForCheckoutUseCase

    @RelaxedMockK
    lateinit var updateBedsUseCase: UpdateBedsUseCase

    @RelaxedMockK
    lateinit var deleteBedForCheckoutUseCase: DeleteBedForCheckoutUseCase

    @RelaxedMockK
    lateinit var getDormsUseCase: GetStoredDormsUseCase

    @RelaxedMockK
    lateinit var updateDormUseCase: UpdateDormUseCase

    @RelaxedMockK
    lateinit var getConversionUseCase: GetConversionUseCase

    private lateinit var checkoutViewModel: CheckoutViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        checkoutViewModel =
            CheckoutViewModel(
                requestSymbolsUseCase,
                getSymbolsUseCase,
                getCartUseCase,
                insertBedForCheckoutUseCase,
                updateBedsUseCase,
                deleteBedForCheckoutUseCase,
                getDormsUseCase,
                updateDormUseCase,
                getConversionUseCase
            )
    }

    @Test
    fun `The app requests symbols to retrieve them on the checkout screen`() =
        runTest {
            checkoutViewModel.state.test {
                val symbolList = listOf("EUR", "GBP", "USD")
                coEvery { requestSymbolsUseCase() }.returns(null)
                every { getSymbolsUseCase() }.returns(flowOf(symbolList))
                assertEquals(CheckoutViewModel.UiState(), awaitItem())
                assertEquals(CheckoutViewModel.UiState(loading = true), awaitItem())
                assertEquals(
                    CheckoutViewModel.UiState(loading = true, symbolList = symbolList),
                    awaitItem()
                )
                cancel()
            }
        }

    @Test
    fun `When the user request a conversion, the app retrieves a new currency and price`() =
        runTest {
            val priceGBP = 14.35
            coEvery { getDormsUseCase() }.returns(listOf(mockedDorm))
            coEvery {
                getConversionUseCase(
                    mockedDorm.currency,
                    TO,
                    mockedDorm.pricePerBed
                )
            }.returns(
                priceGBP.right()
            )
            coEvery {
                updateDormUseCase(
                    mockedDorm.copy(
                        pricePerBed = priceGBP,
                        currency = "GBP",
                        currencySymbol = "£"
                    )
                )
            }.returns(null)
            coEvery { updateBedsUseCase(mockedDorm.id, priceGBP, "GBP", "£") }.returns(null)

            checkoutViewModel.requestConversion(TO, "£")
            runCurrent()

            coVerify { updateBedsUseCase(mockedDorm.id, priceGBP, "GBP", "£") }
        }
}
