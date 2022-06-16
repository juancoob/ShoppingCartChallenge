package com.juancoob.data.datasource

import com.juancoob.domain.Bed
import com.juancoob.domain.Cart
import com.juancoob.domain.Dorm
import kotlinx.coroutines.flow.Flow

interface LocalDormDataSource {
    fun getAvailableDorms(): Flow<List<Dorm>>
    suspend fun getAvailableDormById(id: Int): Dorm
    suspend fun insertDorms(dorms: List<Dorm>)
    suspend fun updateDorm(dorm: Dorm)
    suspend fun storeAvailableBedForCheckout(bed: Bed)
    suspend fun deleteAStoredBedForCheckout(bed: Bed)
    suspend fun getCart(): List<Cart>
}
