package com.juancoob.usecases

import com.juancoob.data.CurrencyRepository
import com.juancoob.domain.ErrorRetrieved
import javax.inject.Inject

class RequestSymbolsUseCase @Inject constructor(private val currencyRepository: CurrencyRepository) {
    suspend operator fun invoke(): ErrorRetrieved? =
        currencyRepository.requestSymbols()
}
