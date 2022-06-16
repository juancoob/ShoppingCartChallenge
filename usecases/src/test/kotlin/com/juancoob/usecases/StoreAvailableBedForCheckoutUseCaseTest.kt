package com.juancoob.usecases

import com.juancoob.data.DormRepository
import com.juancoob.testshared.mockedBed
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StoreAvailableBedForCheckoutUseCaseTest {

    @RelaxedMockK
    lateinit var repository: DormRepository

    lateinit var storeAvailableBedForCheckoutUseCase: StoreAvailableBedForCheckoutUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        storeAvailableBedForCheckoutUseCase = StoreAvailableBedForCheckoutUseCase(repository)
    }

    @Test
    fun `When the user adds beds for checkout, the app store them`() = runTest {
        storeAvailableBedForCheckoutUseCase.invoke(mockedBed)
        coVerify { repository.storeAvailableBedForCheckout(mockedBed) }
    }
}
