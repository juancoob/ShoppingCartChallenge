package com.juancoob.shoppingcartchallenge.data.database

import com.juancoob.testshared.mockedBed
import com.juancoob.testshared.mockedCart
import com.juancoob.testshared.mockedDorm
import org.junit.Assert.assertTrue
import org.junit.Test
import com.juancoob.shoppingcartchallenge.data.database.Bed as DbBed
import com.juancoob.shoppingcartchallenge.data.database.Cart as DbCart

class LocalDataMapperTest {

    @Test
    fun `Maps dorm local model to dorm local db model and inverse to check if the conversion is correct`() {
        val expectedResult = listOf(mockedDorm)
        val dormListDb = expectedResult.fromDomainDormModel()
        val resultedDormList = dormListDb.toDomainDormModel()
        assertTrue(expectedResult == resultedDormList)
    }

    @Test
    fun `Maps local beds to db beds`() {
        val expectedResult = DbBed(
            null,
            mockedBed.dormId,
            mockedBed.pricePerBed,
            mockedBed.currency,
            mockedBed.currencySymbol
        )
        val resultedBed = mockedBed.fromDomainBedModel()
        assertTrue(expectedResult == resultedBed)
    }

    @Test
    fun `Maps db carts to local db carts`() {
        val expectedResult = listOf(mockedCart)
        val listOfDbCart = listOf(
            mockedCart.run {
                DbCart(
                    dormId,
                    bedsForCheckout,
                    type,
                    pricePerBed,
                    bedsAvailable,
                    currency,
                    currencySymbol
                )
            }
        )
        val resultedLocalCartList = listOfDbCart.toDomainCartModel()
        assertTrue(expectedResult == resultedLocalCartList)
    }
}
