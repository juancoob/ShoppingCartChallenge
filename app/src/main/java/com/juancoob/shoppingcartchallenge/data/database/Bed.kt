package com.juancoob.shoppingcartchallenge.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bed(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val dormId: Int,
    val pricePerBed: Double,
    val currency: String,
    val currencySymbol: String
)
