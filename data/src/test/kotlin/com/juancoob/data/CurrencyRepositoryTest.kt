package com.juancoob.data

import arrow.core.left
import arrow.core.right
import com.juancoob.data.datasource.LocalCurrencyDataSource
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.testshared.FROM
import com.juancoob.testshared.TO
import com.juancoob.testshared.mockedBed
import com.juancoob.testshared.mockedDorm
import com.juancoob.testshared.mockedSymbols
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyRepositoryTest {

    @RelaxedMockK
    lateinit var remoteDataSource: RemoteDataSource

    @RelaxedMockK
    lateinit var localCurrencyDataSource: LocalCurrencyDataSource

    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        currencyRepository = CurrencyRepository(remoteDataSource, localCurrencyDataSource)
    }

    @Test
    fun `When the symbol list is empty, the app requests symbols to insert`() = runTest {
        coEvery { localCurrencyDataSource.isSymbolListEmpty() }.returns(true)
        coEvery { remoteDataSource.getSymbols() }.returns(mockedSymbols.right())
        val mockedSymbolList = currencyRepository.run {
            mockedSymbols.toMap().map { "${it.key.uppercase()} - ${it.value}" }
        }

        currencyRepository.requestSymbols()

        coVerify { localCurrencyDataSource.insertSymbols(mockedSymbolList) }
    }

    @Test
    fun `When the symbol list is empty and the symbols request fails, it returns an ErrorRetrieved to inform`() =
        runTest {
            coEvery { localCurrencyDataSource.isSymbolListEmpty() }.returns(true)
            coEvery { remoteDataSource.getSymbols() }.returns(ErrorRetrieved.Connectivity.left())

            val result: ErrorRetrieved? = currencyRepository.requestSymbols()

            assertTrue(result == ErrorRetrieved.Connectivity)
        }

    @Test
    fun `When the symbol list is not empty, it return null and do nothing`() = runTest {
        coEvery { localCurrencyDataSource.isSymbolListEmpty() }.returns(false)

        val result: ErrorRetrieved? = currencyRepository.requestSymbols()

        assertTrue(result == null)
    }

    @Test
    fun `When the user opens the checkout screen, the app loads the symbols requested`() {
        currencyRepository.getSymbols()
        verify { localCurrencyDataSource.getSymbols() }
    }

    @Test
    fun `When the user request the conversion between currencies, the app calls getConversion method`() =
        runTest {
            currencyRepository.getConversion(FROM, TO, mockedDorm.pricePerBed)
            coVerify { remoteDataSource.getConversion(FROM, TO, mockedDorm.pricePerBed) }
        }

    @Test
    fun `When the app retrieves a data class, the app converts it to map`() {
        val expectedMap = mapOf(
            "currency" to "USD",
            "currencySymbol" to "$",
            "dormId" to 1,
            "pricePerBed" to 17.56
        )

        val resultedList = currencyRepository.run { mockedBed.toMap() }

        assertTrue(expectedMap == resultedList)
    }
}
