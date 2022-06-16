package com.juancoob.data

import arrow.core.Either
import com.juancoob.data.datasource.LocalCurrencyDataSource
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.domain.ErrorRetrieved
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localCurrencyDataSource: LocalCurrencyDataSource
) {
    suspend fun requestSymbols(): ErrorRetrieved? {
        if (localCurrencyDataSource.isSymbolListEmpty()) {
            remoteDataSource.getSymbols().fold(ifLeft = { return it }) {
                localCurrencyDataSource.insertSymbols(it)
            }
        }
        return null
    }

    fun getSymbols(): Flow<List<String>> = localCurrencyDataSource.getSymbols()

    suspend fun deleteSymbols() = localCurrencyDataSource.deleteAllSymbols()

    suspend fun getConversion(
        from: String,
        to: String,
        amount: Double
    ): Either<ErrorRetrieved, Double> =
        remoteDataSource.getConversion(from, to, amount)
}
