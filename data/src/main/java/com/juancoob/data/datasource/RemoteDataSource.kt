package com.juancoob.data.datasource

import arrow.core.Either
import com.juancoob.domain.ErrorRetrieved

interface RemoteDataSource {
    suspend fun getSymbols(): Either<ErrorRetrieved, List<String>>
    suspend fun getConversion(from: String, to: String, amount: Double): Either<ErrorRetrieved, Double>
}
