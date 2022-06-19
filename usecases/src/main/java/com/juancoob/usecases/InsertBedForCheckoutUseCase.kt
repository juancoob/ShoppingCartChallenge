package com.juancoob.usecases

import com.juancoob.data.DormRepository
import com.juancoob.domain.Bed
import javax.inject.Inject

class InsertBedForCheckoutUseCase @Inject constructor(private val repository: DormRepository) {
    suspend operator fun invoke(bed: Bed) =
        repository.insertBedForCheckout(bed)
}
