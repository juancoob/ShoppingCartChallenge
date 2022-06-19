package com.juancoob.usecases

import com.juancoob.data.DormRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetStoredDormsUseCaseTest {

    @RelaxedMockK
    lateinit var repository: DormRepository

    lateinit var getStoredDormsUseCase: GetStoredDormsUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        getStoredDormsUseCase = GetStoredDormsUseCase(repository)
    }

    @Test
    fun `When the user opens the app, it checks the dorms already stored`() = runTest {
        getStoredDormsUseCase.invoke()
        coVerify { repository.getStoredDorms() }
    }
}
