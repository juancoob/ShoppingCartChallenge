package com.juancoob.shoppingcartchallenge.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bed(
    @PrimaryKey val dormId: Int,
    val pricePerBed: Double,
    val currency: String
)
