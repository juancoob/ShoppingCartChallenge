package com.juancoob.shoppingcartchallenge.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dorm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val maxBeds: Int,
    val bedsAvailable: Int,
    val pricePerBed: Double
)
