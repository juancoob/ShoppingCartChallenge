package com.juancoob.usecases

import com.juancoob.data.DormRepository
import com.juancoob.domain.ErrorRetrieved
import javax.inject.Inject

class UpdateBedsUseCase @Inject constructor(private val repository: DormRepository) {
    suspend operator fun invoke(
        dormId: Int,
        pricePerBed: Double,
        currency: String,
        currencySymbol: String
    ): ErrorRetrieved? = repository.updateBedsCurrency(dormId, pricePerBed, currency, currencySymbol)
}
