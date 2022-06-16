package com.juancoob.domain

data class Dorm(
    val id: Int,
    val type: String,
    val maxBeds: Int,
    val bedsAvailable: Int,
    val pricePerBed: Double,
    val currency: String
)
