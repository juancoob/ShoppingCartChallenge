package com.juancoob.usecases

import com.juancoob.data.DormRepository
import com.juancoob.domain.ErrorRetrieved
import javax.inject.Inject

class DeleteBedForCheckoutUseCase @Inject constructor(private val repository: DormRepository) {
    suspend operator fun invoke(dormId: Int): ErrorRetrieved? =
        repository.deleteBedForCheckout(dormId)
}
