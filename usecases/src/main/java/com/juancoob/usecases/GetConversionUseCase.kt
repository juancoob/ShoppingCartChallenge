package com.juancoob.usecases

import arrow.core.Either
import com.juancoob.data.CurrencyRepository
import com.juancoob.domain.ErrorRetrieved
import javax.inject.Inject

class GetConversionUseCase @Inject constructor(private val repository: CurrencyRepository) {
    suspend operator fun invoke(
        from: String,
        to: String,
        amount: Double
    ): Either<ErrorRetrieved, Double> =
        repository.getConversion(from, to, amount)
}
