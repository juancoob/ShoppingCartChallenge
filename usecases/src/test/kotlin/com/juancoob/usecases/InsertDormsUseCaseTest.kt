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
class InsertDormsUseCaseTest {

    @RelaxedMockK
    lateinit var repository: DormRepository

    lateinit var insertDormsUseCase: InsertDormsUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        insertDormsUseCase = InsertDormsUseCase(repository)
    }

    @Test
    fun `When the app starts, it adds dorms`() =
        runTest {
            insertDormsUseCase.invoke(listOf(mockedDorm))
            coVerify { repository.insertDorms(listOf(mockedDorm)) }
        }
}
