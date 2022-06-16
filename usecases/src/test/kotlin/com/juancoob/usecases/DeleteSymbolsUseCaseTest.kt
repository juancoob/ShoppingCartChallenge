package com.juancoob.usecases

import com.juancoob.data.CurrencyRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteSymbolsUseCaseTest {

    @RelaxedMockK
    lateinit var repository: CurrencyRepository

    lateinit var deleteSymbolsUseCase: DeleteSymbolsUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        deleteSymbolsUseCase = DeleteSymbolsUseCase(repository)
    }

    @Test
    fun `When the user destroys the app, it calls to delete symbols`() = runTest {
        deleteSymbolsUseCase.invoke()
        coVerify { repository.deleteSymbols() }
    }
}
