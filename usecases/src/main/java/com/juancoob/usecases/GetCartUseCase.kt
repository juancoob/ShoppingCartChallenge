package com.juancoob.usecases

import com.juancoob.data.DormRepository
import com.juancoob.domain.Cart
import javax.inject.Inject

class GetCartUseCase @Inject constructor(private val repository: DormRepository) {
    suspend operator fun invoke(): List<Cart> =
        repository.getCart()
}
