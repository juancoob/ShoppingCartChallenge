package com.juancoob.shoppingcartchallenge.data.server

import kotlinx.serialization.Serializable

@Serializable
data class ConversionPayload (
    val success: Boolean,
    val result: Double
)
