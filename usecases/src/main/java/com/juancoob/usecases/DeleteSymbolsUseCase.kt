package com.juancoob.usecases

import com.juancoob.data.CurrencyRepository
import javax.inject.Inject

class DeleteSymbolsUseCase @Inject constructor(private val repository: CurrencyRepository) {
    suspend operator fun invoke() =
        repository.deleteSymbols()
}
