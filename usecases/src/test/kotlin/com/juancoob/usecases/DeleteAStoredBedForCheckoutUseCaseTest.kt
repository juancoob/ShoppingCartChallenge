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
class DeleteAStoredBedForCheckoutUseCaseTest {

    @RelaxedMockK
    lateinit var repository: DormRepository

    lateinit var deleteAStoredBedForCheckoutUseCase: DeleteAStoredBedForCheckoutUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        deleteAStoredBedForCheckoutUseCase = DeleteAStoredBedForCheckoutUseCase(repository)
    }

    @Test
    fun `When the user deletes a bed from the counter, it calls to delete the bed`() = runTest {
        deleteAStoredBedForCheckoutUseCase.invoke(mockedBed)
        coVerify { repository.deleteAStoredBedForCheckout(mockedBed) }
    }
}
