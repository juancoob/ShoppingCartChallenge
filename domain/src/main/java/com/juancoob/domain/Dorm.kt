package com.juancoob.domain

data class Dorm(
    val id: Int,
    val maxBeds: Int,
    val bedsAvailable: Int,
    val pricePerBed: Float
)
