package com.juancoob.shoppingcartchallenge.data.server


import kotlinx.serialization.Serializable

@Serializable
data class SymbolsPayload(
    val success: Boolean,
    val symbols: List<String>
)
