package com.juancoob.shoppingcartchallenge.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart(
    @PrimaryKey val dormId: Int,
    val bedsForCheckout: Int,
    val type: String,
    val pricePerBed: Double,
    val bedsAvailable: Int,
    val currency: String,
    val currencySymbol: String
)
