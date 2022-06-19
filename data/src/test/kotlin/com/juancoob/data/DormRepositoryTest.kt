package com.juancoob.data

import com.juancoob.data.datasource.LocalDormDataSource
import com.juancoob.testshared.mockedBed
import com.juancoob.testshared.mockedDorm
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DormRepositoryTest {

    @RelaxedMockK
    lateinit var localDormDataSource: LocalDormDataSource

    private lateinit var dormRepository: DormRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dormRepository = DormRepository(localDormDataSource)
    }

    @Test
    fun `When the app starts, it retrieves the available dorms`() {
        dormRepository.getAvailableDorms()
        verify { localDormDataSource.getAvailableDorms() }
    }

    @Test
    fun `When the user clicks on an available dorm, the app will retrieve the dorm`() {
        dormRepository.getAvailableDormById(mockedDorm.id)
        verify { localDormDataSource.getAvailableDormById(mockedDorm.id) }
    }

    @Test
    fun `When the user opens the app, it checks the number of dorms already stored`() = runTest {
        dormRepository.getStoredDorms()
        coVerify { localDormDataSource.getStoredDorms() }
    }

    @Test
    fun `When the app starts, it inserts dorms`() = runTest {
        dormRepository.insertDorms(listOf(mockedDorm))
        coVerify { localDormDataSource.insertDorms(listOf(mockedDorm)) }
    }

    @Test
    fun `When the user updates the available beds, the app updates the dorm`() = runTest {
        dormRepository.updateDorm(mockedDorm)
        coVerify { localDormDataSource.updateDorm(mockedDorm) }
    }

    @Test
    fun `When the user picks out beds, the app stores them for the checkout screen`() = runTest {
        dormRepository.insertBedForCheckout(mockedBed)
        coVerify { localDormDataSource.insertBedForCheckout(mockedBed) }
    }

    @Test
    fun `When the user changes the currency, the app updates the bed currency`() =
        runTest {
            mockedDorm.run {
                dormRepository.updateBedsCurrency(id, pricePerBed, currency, currencySymbol)
                coVerify {
                    localDormDataSource.updateBedsCurrency(
                        id,
                        pricePerBed,
                        currency,
                        currencySymbol
                    )
                }
            }
        }

    @Test
    fun `When the user deletes a bed previously selected for checkout, the app calls to delete the bed`() =
        runTest {
            dormRepository.deleteBedForCheckout(mockedDorm.id)
            coVerify { localDormDataSource.deleteBedForCheckout(mockedDorm.id) }
        }

    @Test
    fun `When the user opens the checkout screen, the app loads the cart content`() {
        dormRepository.getCart()
        verify { localDormDataSource.getCart() }
    }
}
