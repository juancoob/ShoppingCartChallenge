package com.juancoob.usecases

import com.juancoob.data.DormRepository
import javax.inject.Inject

class UpdateBedsUseCase @Inject constructor(private val repository: DormRepository) {
    suspend operator fun invoke(
        dormId: Int,
        pricePerBed: Double,
        currency: String,
        currencySymbol: String
    ) = repository.updateBedsCurrency(dormId, pricePerBed, currency, currencySymbol)
}
