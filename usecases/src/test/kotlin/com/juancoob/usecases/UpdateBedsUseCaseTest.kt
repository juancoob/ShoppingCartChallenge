package com.juancoob.usecases

import com.juancoob.data.DormRepository
import com.juancoob.testshared.mockedDorm
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateBedsUseCaseTest {

    @RelaxedMockK
    lateinit var repository: DormRepository

    lateinit var updateBedsUseCase: UpdateBedsUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        updateBedsUseCase = UpdateBedsUseCase(repository)
    }

    @Test
    fun `When the user selects a currency, the app updates the bed currency`() =
        runTest {
            mockedDorm.run {
                updateBedsUseCase.invoke(id, pricePerBed, currency, currencySymbol)
                coVerify { repository.updateBedsCurrency(id, pricePerBed, currency, currencySymbol) }
            }
        }
}
