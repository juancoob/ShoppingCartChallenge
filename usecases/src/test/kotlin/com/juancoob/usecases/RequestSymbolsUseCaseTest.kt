package com.juancoob.usecases

import com.juancoob.data.CurrencyRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RequestSymbolsUseCaseTest {

    @RelaxedMockK
    lateinit var repository: CurrencyRepository

    lateinit var requestSymbolsUseCase: RequestSymbolsUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        requestSymbolsUseCase = RequestSymbolsUseCase(repository)
    }

    @Test
    fun `When the user opens the checkout screen, the app requests the available symbols`() = runTest {
        requestSymbolsUseCase.invoke()
        coVerify { repository.requestSymbols() }
    }
}
