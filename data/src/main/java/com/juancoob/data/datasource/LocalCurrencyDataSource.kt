package com.juancoob.data.datasource

import com.juancoob.domain.ErrorRetrieved
import kotlinx.coroutines.flow.Flow

interface LocalCurrencyDataSource {
    fun getSymbols(): Flow<List<String>>
    suspend fun isSymbolListEmpty(): Boolean
    suspend fun insertSymbols(symbols: List<String>): ErrorRetrieved?
    suspend fun deleteAllSymbols()
}
