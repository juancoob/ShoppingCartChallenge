package com.juancoob.usecases

import com.juancoob.data.DormRepository
import com.juancoob.domain.Cart
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(private val repository: DormRepository) {
    operator fun invoke(): Flow<List<Cart>> =
        repository.getCart()
}
