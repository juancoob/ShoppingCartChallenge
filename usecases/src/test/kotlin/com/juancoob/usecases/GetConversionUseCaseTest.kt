package com.juancoob.usecases

import com.juancoob.data.CurrencyRepository
import com.juancoob.testshared.FROM
import com.juancoob.testshared.TO
import com.juancoob.testshared.mockedBed
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetConversionUseCaseTest {

    @RelaxedMockK
    lateinit var repository: CurrencyRepository

    lateinit var getConversionUseCase: GetConversionUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        getConversionUseCase = GetConversionUseCase(repository)
    }

    @Test
    fun `When the user changes the currency, the app calls the conversion method`() = runTest {
        getConversionUseCase.invoke(FROM, TO, mockedBed.pricePerBed)
        coVerify { repository.getConversion(FROM, TO, mockedBed.pricePerBed) }
    }
}
