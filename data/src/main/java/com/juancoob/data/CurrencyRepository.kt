package com.juancoob.data

import arrow.core.Either
import com.juancoob.data.datasource.LocalCurrencyDataSource
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.domain.ErrorRetrieved
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class CurrencyRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localCurrencyDataSource: LocalCurrencyDataSource
) {
    suspend fun requestSymbols(): ErrorRetrieved? {
        if (localCurrencyDataSource.isSymbolListEmpty()) {
            remoteDataSource.getSymbols().fold(ifLeft = { return it }) { symbols ->
                val symbolList = symbols.toMap().map { "${it.key.uppercase()} - ${it.value}" }
                localCurrencyDataSource.insertSymbols(symbolList)
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

    fun <T : Any> T.toMap(): Map<String, Any?> {
        return (this::class as KClass<T>).memberProperties.associate { prop ->
            prop.name to prop.get(this)?.let { value ->
                if (value::class.isData) {
                    value.toMap()
                } else {
                    value
                }
            }
        }
    }
}
