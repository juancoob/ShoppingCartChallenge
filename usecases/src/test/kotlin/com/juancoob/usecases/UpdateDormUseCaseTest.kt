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
class UpdateDormUseCaseTest {

    @RelaxedMockK
    lateinit var repository: DormRepository

    lateinit var updateDormUseCase: UpdateDormUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        updateDormUseCase = UpdateDormUseCase(repository)
    }

    @Test
    fun `When the available beds change, the app updates the dorm`() =
        runTest {
            updateDormUseCase.invoke(mockedDorm)
            coVerify { repository.updateDorm(mockedDorm) }
        }
}
