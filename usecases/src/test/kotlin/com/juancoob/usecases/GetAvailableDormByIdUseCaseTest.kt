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

@OptIn(ExperimentalCoroutinesApi::class)
class GetAvailableDormByIdUseCaseTest {

    @RelaxedMockK
    lateinit var repository: DormRepository

    lateinit var getAvailableDormByIdUseCase: GetAvailableDormByIdUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        getAvailableDormByIdUseCase = GetAvailableDormByIdUseCase(repository)
    }

    @Test
    fun `When the user clicks on a dorm in the main list, the app retrieves its details`() =
        runTest {
            getAvailableDormByIdUseCase.invoke(mockedDorm.id)
            coVerify { repository.getAvailableDormById(mockedDorm.id) }
        }
}
