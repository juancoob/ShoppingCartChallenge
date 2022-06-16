package com.juancoob.usecases

import com.juancoob.data.CurrencyRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetSymbolsUseCaseTest {

    @RelaxedMockK
    lateinit var repository: CurrencyRepository

    lateinit var getSymbolsUseCase: GetSymbolsUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        getSymbolsUseCase = GetSymbolsUseCase(repository)
    }

    @Test
    fun `When the user opens the checkout screen, the app gets the available currency symbols`() {
        getSymbolsUseCase.invoke()
        verify { repository.getSymbols() }
    }
}
