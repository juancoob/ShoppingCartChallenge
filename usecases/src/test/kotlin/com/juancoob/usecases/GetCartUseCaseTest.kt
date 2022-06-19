package com.juancoob.usecases

import com.juancoob.data.DormRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetCartUseCaseTest {

    @RelaxedMockK
    lateinit var repository: DormRepository

    lateinit var getCartUseCase: GetCartUseCase

    @Before
    fun startUp() {
        MockKAnnotations.init(this)
        getCartUseCase = GetCartUseCase(repository)
    }

    @Test
    fun `When the user opens the checkout screen, the app calls to the cart content`() {
        getCartUseCase.invoke()
        verify { repository.getCart() }
    }
}
