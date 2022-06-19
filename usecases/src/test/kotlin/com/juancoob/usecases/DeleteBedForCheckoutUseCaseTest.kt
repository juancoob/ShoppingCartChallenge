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
class DeleteBedForCheckoutUseCaseTest {

    @RelaxedMockK
    lateinit var repository: DormRepository

    lateinit var deleteBedForCheckoutUseCase: DeleteBedForCheckoutUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        deleteBedForCheckoutUseCase = DeleteBedForCheckoutUseCase(repository)
    }

    @Test
    fun `When the user deletes a bed from the counter, it calls to delete the bed`() = runTest {
        deleteBedForCheckoutUseCase.invoke(mockedDorm.id)
        coVerify { repository.deleteBedForCheckout(mockedDorm.id) }
    }
}
