package com.juancoob.usecases

import com.juancoob.data.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSymbolsUseCase @Inject constructor(private val repository: CurrencyRepository) {
    operator fun invoke(): Flow<List<String>> =
        repository.getSymbols()
}
