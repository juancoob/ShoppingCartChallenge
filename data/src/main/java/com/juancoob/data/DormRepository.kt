package com.juancoob.data

import com.juancoob.data.datasource.LocalDormDataSource
import com.juancoob.domain.Bed
import com.juancoob.domain.Cart
import com.juancoob.domain.Dorm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DormRepository @Inject constructor(
    private val localDormDataSource: LocalDormDataSource
) {
    fun getAvailableDorms(): Flow<List<Dorm>> =
        localDormDataSource.getAvailableDorms()

    suspend fun getAvailableDormById(id: Int): Dorm =
        localDormDataSource.getAvailableDormById(id)

    suspend fun insertDorms(dorms: List<Dorm>) =
        localDormDataSource.insertDorms(dorms)

    suspend fun updateDorm(dorm: Dorm) =
        localDormDataSource.updateDorm(dorm)

    suspend fun storeAvailableBedForCheckout(bed: Bed) =
        localDormDataSource.storeAvailableBedForCheckout(bed)

    suspend fun deleteAStoredBedForCheckout(bed: Bed) =
        localDormDataSource.deleteAStoredBedForCheckout(bed)

    suspend fun getCart(): List<Cart> =
        localDormDataSource.getCart()
}
