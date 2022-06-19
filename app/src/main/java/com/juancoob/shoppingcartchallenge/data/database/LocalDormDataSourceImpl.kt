package com.juancoob.shoppingcartchallenge.data.database

import com.juancoob.data.datasource.LocalDormDataSource
import com.juancoob.domain.Bed
import com.juancoob.domain.Cart
import com.juancoob.domain.Dorm
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.shoppingcartchallenge.data.tryCall
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
        dormDao.getAvailableDormById(id).map { it.toDomainDormModel() }

    override suspend fun getStoredDorms(): List<Dorm> =
        dormDao.getStoredDorms().toDomainDormModel()

    override suspend fun insertDorms(dorms: List<Dorm>): ErrorRetrieved? = tryCall {
        dormDao.insertDorms(dorms.fromDomainDormModel())
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun updateDorm(dorm: Dorm): ErrorRetrieved? = tryCall {
        dormDao.updateDorm(dorm.fromDomainDormModel())
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun insertBedForCheckout(bed: Bed): ErrorRetrieved? = tryCall {
        dormDao.insertBedForCheckout(bed.fromDomainBedModel())
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun updateBedsCurrency(
        dormId: Int,
        pricePerBed: Double,
        currency: String,
        currencySymbol: String
    ): ErrorRetrieved? = tryCall {
        dormDao.updateBedsCurrency(dormId, pricePerBed, currency, currencySymbol)
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun deleteBedForCheckout(dormId: Int) = tryCall {
        dormDao.deleteBedForCheckout(dormId)
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override fun getCart(): Flow<List<Cart>> =
        dormDao.getCart().map { it.toDomainCartModel() }
}

fun List<DbDorm>.toDomainDormModel(): List<Dorm> = map { it.toDomainDormModel() }

fun DbDorm.toDomainDormModel(): Dorm = Dorm(
    id = id,
    type = type,
    bedsAvailable = bedsAvailable,
    pricePerBed = pricePerBed,
    currency = currency,
    currencySymbol = currencySymbol
)

fun List<Dorm>.fromDomainDormModel(): List<DbDorm> = map { it.fromDomainDormModel() }

fun Dorm.fromDomainDormModel(): DbDorm = DbDorm(
    id = id,
    type = type,
    bedsAvailable = bedsAvailable,
    pricePerBed = pricePerBed,
    currency = currency,
    currencySymbol = currencySymbol
)

fun Bed.fromDomainBedModel(): DbBed = DbBed(
    dormId = dormId,
    pricePerBed = pricePerBed,
    currency = currency,
    currencySymbol = currencySymbol
)

fun List<DbCart>.toDomainCartModel(): List<Cart> = map { it.toDomainCartModel() }

fun DbCart.toDomainCartModel(): Cart = Cart(
    dormId = dormId,
    bedsForCheckout = bedsForCheckout,
    type = type,
    pricePerBed = pricePerBed,
    bedsAvailable = bedsAvailable,
    currency = currency,
    currencySymbol = currencySymbol
)
