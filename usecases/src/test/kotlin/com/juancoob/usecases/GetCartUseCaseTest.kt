package com.juancoob.usecases

import com.juancoob.data.DormRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
    fun `When the user opens the checkout screen, the app calls to the cart content`() = runTest {
        getCartUseCase.invoke()
        coVerify { repository.getCart() }
    }
}
