package com.juancoob.testshared

import com.juancoob.domain.Bed
import com.juancoob.domain.Cart
import com.juancoob.domain.Dorm

const val FROM = "EUR"

const val TO = "GBP"

val mockedSymbols = listOf("EUR", "GBP")

val mockedDorm = Dorm(
    id = 1,
    type = "6-bed dorm",
    maxBeds = 6,
    bedsAvailable = 6,
    pricePerBed = 17.56
)

val mockedBed = Bed(
    dormId = 1,
    pricePerBed = 17.56
)

val mockedCart = Cart(
    dormId = 1,
    bedsForCheckout = 1,
    type = "6-bed dorm",
    pricePerBed = 17.56,
    bedsAvailable = 5
)
