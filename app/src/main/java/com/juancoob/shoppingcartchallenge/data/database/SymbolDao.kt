package com.juancoob.shoppingcartchallenge.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SymbolDao {
    @Query("SELECT * FROM Symbol")
    fun getSymbols(): Flow<List<Symbol>>

    @Query("SELECT COUNT(id) from Symbol")
    suspend fun getSymbolListSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymbols(symbols: List<Symbol>)

    @Query("DELETE FROM Symbol")
    suspend fun deleteAllSymbols()

}
