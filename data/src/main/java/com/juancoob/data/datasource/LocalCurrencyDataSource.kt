package com.juancoob.data.datasource

import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Symbol
import kotlinx.coroutines.flow.Flow

interface LocalCurrencyDataSource {
    fun getSymbols(): Flow<List<String>>
    suspend fun isSymbolListEmpty(): Boolean
    suspend fun insertSymbols(symbolList: List<String>): ErrorRetrieved?
}
