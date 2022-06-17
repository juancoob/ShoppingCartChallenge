package com.juancoob.data.datasource

import arrow.core.Either
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.domain.Symbol

interface RemoteDataSource {
    suspend fun getSymbols(): Either<ErrorRetrieved, Symbol>
    suspend fun getConversion(from: String, to: String, amount: Double): Either<ErrorRetrieved, Double>
}
