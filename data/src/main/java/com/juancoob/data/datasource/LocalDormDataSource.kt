package com.juancoob.data.datasource

import com.juancoob.domain.Bed
import com.juancoob.domain.Cart
import com.juancoob.domain.Dorm
import com.juancoob.domain.ErrorRetrieved
import kotlinx.coroutines.flow.Flow

interface LocalDormDataSource {
    fun getAvailableDorms(): Flow<List<Dorm>>
    fun getAvailableDormById(id: Int): Flow<Dorm>
    suspend fun getStoredDorms(): List<Dorm>
    suspend fun insertDorms(dorms: List<Dorm>): ErrorRetrieved?
    suspend fun updateDorm(dorm: Dorm): ErrorRetrieved?
    suspend fun insertBedForCheckout(bed: Bed): ErrorRetrieved?
    suspend fun updateBedsCurrency(
        dormId: Int,
        pricePerBed: Double,
        currency: String,
        currencySymbol: String
    ): ErrorRetrieved?
    suspend fun deleteBedForCheckout(dormId: Int): ErrorRetrieved?
    fun getCart(): Flow<List<Cart>>
}
