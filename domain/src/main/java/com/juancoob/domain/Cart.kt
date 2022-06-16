package com.juancoob.domain

data class Cart(
    val dormId: Int,
    val bedsForCheckout: Int,
    val type: String,
    val pricePerBed: Double,
    val bedsAvailable: Int
)
