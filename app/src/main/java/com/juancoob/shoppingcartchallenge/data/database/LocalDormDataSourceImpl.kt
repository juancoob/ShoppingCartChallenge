package com.juancoob.shoppingcartchallenge.data.database

import com.juancoob.data.datasource.LocalDormDataSource
import com.juancoob.domain.Bed
import com.juancoob.domain.Cart
import com.juancoob.domain.Dorm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.juancoob.shoppingcartchallenge.data.database.Bed as DbBed
import com.juancoob.shoppingcartchallenge.data.database.Cart as DbCart
import com.juancoob.shoppingcartchallenge.data.database.Dorm as DbDorm

class LocalDormDataSourceImpl @Inject constructor(
    private val dormDao: DormDao
) : LocalDormDataSource {

    override fun getAvailableDorms(): Flow<List<Dorm>> =
        dormDao.getAvailableDorms().map { it.toDomainDormModel() }

    override fun getAvailableDormById(id: Int): Flow<Dorm> =
        dormDao.getAvailableDormById(id).map{ it.toDomainDormModel()}

    override suspend fun getStoredDorms(): Int =
        dormDao.getStoredDorms()

    override suspend fun insertDorms(dorms: List<Dorm>) =
        dormDao.insertDorms(dorms.fromDomainDormModel())

    override suspend fun updateDorm(dorm: Dorm) =
        dormDao.updateDorm(dorm.fromDomainDormModel())

    override suspend fun storeAvailableBedForCheckout(bed: Bed) =
        dormDao.storeAvailableBedForCheckout(bed.fromDomainBedModel())

    override suspend fun deleteAStoredBedForCheckout(bed: Bed) =
        dormDao.deleteAStoredBedForCheckout(bed.fromDomainBedModel())

    override suspend fun getCart(): List<Cart> =
        dormDao.getCart().toDomainCartModel()
}

fun List<DbDorm>.toDomainDormModel(): List<Dorm> = map { it.toDomainDormModel() }

fun DbDorm.toDomainDormModel(): Dorm = Dorm(
    id = id,
    type = type,
    maxBeds = maxBeds,
    bedsAvailable = bedsAvailable,
    pricePerBed = pricePerBed,
    currency = currency
)

fun List<Dorm>.fromDomainDormModel(): List<DbDorm> = map { it.fromDomainDormModel() }

fun Dorm.fromDomainDormModel(): DbDorm = DbDorm(
    id = id,
    type = type,
    maxBeds = maxBeds,
    bedsAvailable = bedsAvailable,
    pricePerBed = pricePerBed,
    currency = currency
)

fun Bed.fromDomainBedModel(): DbBed = DbBed(
    dormId = dormId,
    pricePerBed = pricePerBed,
    currency = currency
)

fun List<DbCart>.toDomainCartModel(): List<Cart> = map { it.toDomainCartModel() }

fun DbCart.toDomainCartModel(): Cart = Cart(
    dormId = dormId,
    bedsForCheckout = bedsForCheckout,
    type = type,
    pricePerBed = pricePerBed,
    bedsAvailable = bedsAvailable,
    currency = currency
)
