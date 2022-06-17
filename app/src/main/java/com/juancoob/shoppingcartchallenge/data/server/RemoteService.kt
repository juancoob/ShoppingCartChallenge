package com.juancoob.shoppingcartchallenge.data.server

import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {

    @GET("symbols")
    suspend fun getSymbols(
       @Query("apikey") apikey: String
    ): SymbolsPayload

    @GET("convert")
    suspend fun getConversion(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String
    ): ConversionPayload
}
