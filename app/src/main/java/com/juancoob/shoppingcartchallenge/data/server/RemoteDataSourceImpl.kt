package com.juancoob.shoppingcartchallenge.data.server

import arrow.core.Either
import com.juancoob.data.datasource.RemoteDataSource
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.shoppingcartchallenge.tryCall

class RemoteDataSourceImpl(
    private val apiKey: String,
    private val remoteService: RemoteService
) : RemoteDataSource {

    override suspend fun getSymbols(): Either<ErrorRetrieved, List<String>> = tryCall {
        remoteService.getSymbols(apiKey).symbols
    }

    override suspend fun getConversion(
        from: String,
        to: String,
        amount: Double
    ): Either<ErrorRetrieved, Double> = tryCall {
        remoteService.getConversion(from, to, amount.toString()).result
    }
}
