package com.juancoob.shoppingcartchallenge.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DormDao {
    @Query("SELECT * FROM Dorm WHERE bedsAvailable > 0")
    fun getAvailableDorms(): Flow<List<Dorm>>

    @Query("SELECT * FROM Dorm WHERE id = :id")
    fun getAvailableDormById(id: Int): Flow<Dorm>

    @Query("SELECT * FROM Dorm")
    suspend fun getStoredDorms(): List<Dorm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDorms(dorms: List<Dorm>)

    @Update
    suspend fun updateDorm(dorm: Dorm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBedForCheckout(bed: Bed)

    @Query(
        "UPDATE Bed SET pricePerBed = :pricePerBed, currency = :currency, currencySymbol = :currencySymbol " +
                "WHERE dormId = :dormId"
    )
    suspend fun updateBedsCurrency(
        dormId: Int,
        pricePerBed: Double,
        currency: String,
        currencySymbol: String
    )

    @Query("DELETE FROM Bed WHERE id IN (SELECT id FROM Bed WHERE dormId = :dormId LIMIT 1)")
    suspend fun deleteBedForCheckout(dormId: Int)

    @Query("SELECT dormId, COUNT(Bed.dormId) AS bedsForCheckout, type, Dorm.pricePerBed, bedsAvailable, Dorm.currency" +
            " FROM Bed, Dorm WHERE Bed.dormId = Dorm.id")
    suspend fun getCart(): List<Cart>
}
