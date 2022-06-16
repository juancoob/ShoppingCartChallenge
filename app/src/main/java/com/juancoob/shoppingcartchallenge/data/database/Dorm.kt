package com.juancoob.shoppingcartchallenge.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dorm(
    @PrimaryKey val id: Int,
    val type: String,
    val maxBeds: Int,
    val bedsAvailable: Int,
    val pricePerBed: Double,
    val currency: String
)
