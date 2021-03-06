package com.juancoob.shoppingcartchallenge.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Symbol(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val symbol: String
)
