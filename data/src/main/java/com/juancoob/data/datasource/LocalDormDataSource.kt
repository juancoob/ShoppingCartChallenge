package com.juancoob.data.datasource

import com.juancoob.domain.Bed
import com.juancoob.domain.Cart
import com.juancoob.domain.Dorm
import kotlinx.coroutines.flow.Flow

interface LocalDormDataSource {
    fun getAvailableDorms(): Flow<List<Dorm>>
    fun getAvailableDormById(id: Int): Flow<Dorm>
    suspend fun getStoredDorms(): List<Dorm>
    suspend fun insertDorms(dorms: List<Dorm>)
    suspend fun updateDorm(dorm: Dorm)
    suspend fun insertBedForCheckout(bed: Bed)
    suspend fun updateBedsCurrency(
        dormId: Int,
        pricePerBed: Double,
        currency: String,
        currencySymbol: String
    )
    suspend fun deleteBedForCheckout(dormId: Int)
    fun getCart(): Flow<List<Cart>>
}
