package com.juancoob.shoppingcartchallenge.data.server


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SymbolsPayload(
    @SerialName("success")
    val success: Boolean,
    @SerialName("symbols")
    val symbols: Symbols
)
