package com.juancoob.shoppingcartchallenge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Symbol::class, Bed::class, Dorm::class], version = 1, exportSchema = false)
abstract class ChallengeDatabase : RoomDatabase() {
    abstract fun symbolDao(): SymbolDao
    abstract fun dormDao(): DormDao
}
