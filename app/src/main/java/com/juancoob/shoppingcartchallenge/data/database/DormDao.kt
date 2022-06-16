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

    @Query("SELECT COUNT(id) FROM Dorm")
    suspend fun getStoredDorms(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDorms(dorms: List<Dorm>)

    @Update
    suspend fun updateDorm(dorm: Dorm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storeAvailableBedForCheckout(bed: Bed)

    @Delete
    suspend fun deleteAStoredBedForCheckout(bed: Bed)

    @Query("SELECT dormId, COUNT(Bed.dormId) AS bedsForCheckout, type, Dorm.pricePerBed, bedsAvailable, Dorm.currency" +
            " FROM Bed, Dorm WHERE Bed.dormId = Dorm.id")
    suspend fun getCart(): List<Cart>
}
