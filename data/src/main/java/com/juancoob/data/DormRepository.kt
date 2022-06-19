package com.juancoob.data

import com.juancoob.data.datasource.LocalDormDataSource
import com.juancoob.domain.Bed
import com.juancoob.domain.Cart
import com.juancoob.domain.Dorm
import com.juancoob.domain.ErrorRetrieved
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DormRepository @Inject constructor(
    private val localDormDataSource: LocalDormDataSource
) {
    fun getAvailableDorms(): Flow<List<Dorm>> =
        localDormDataSource.getAvailableDorms()

    fun getAvailableDormById(id: Int): Flow<Dorm> =
        localDormDataSource.getAvailableDormById(id)

    suspend fun getStoredDorms(): List<Dorm> =
        localDormDataSource.getStoredDorms()

    suspend fun insertDorms(dorms: List<Dorm>): ErrorRetrieved? =
        localDormDataSource.insertDorms(dorms)

    suspend fun updateDorm(dorm: Dorm): ErrorRetrieved? =
        localDormDataSource.updateDorm(dorm)

    suspend fun insertBedForCheckout(bed: Bed): ErrorRetrieved? =
        localDormDataSource.insertBedForCheckout(bed)

    suspend fun updateBedsCurrency(
        dormId: Int,
        pricePerBed: Double,
        currency: String,
        currencySymbol: String
    ): ErrorRetrieved? = localDormDataSource.updateBedsCurrency(dormId, pricePerBed, currency, currencySymbol)

    suspend fun deleteBedForCheckout(dormId: Int): ErrorRetrieved? =
        localDormDataSource.deleteBedForCheckout(dormId)

    fun getCart(): Flow<List<Cart>> =
        localDormDataSource.getCart()
}
