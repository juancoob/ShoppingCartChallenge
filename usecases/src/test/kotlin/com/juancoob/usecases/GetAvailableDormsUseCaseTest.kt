package com.juancoob.usecases

import com.juancoob.data.DormRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetAvailableDormsUseCaseTest {

    @RelaxedMockK
    lateinit var repository: DormRepository

    lateinit var getAvailableDormsUseCase: GetAvailableDormsUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        getAvailableDormsUseCase = GetAvailableDormsUseCase(repository)
    }

    @Test
    fun `When the user opens the app, it retrieves the dorms`() {
        getAvailableDormsUseCase.invoke()
        verify { repository.getAvailableDorms() }
    }
}
