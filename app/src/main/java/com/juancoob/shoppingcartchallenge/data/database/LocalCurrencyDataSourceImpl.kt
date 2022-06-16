package com.juancoob.shoppingcartchallenge.data.database

import com.juancoob.data.datasource.LocalCurrencyDataSource
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.shoppingcartchallenge.data.tryCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalCurrencyDataSourceImpl @Inject constructor(
    private val symbolDao: SymbolDao,
) : LocalCurrencyDataSource {
    override fun getSymbols(): Flow<List<String>> =
        symbolDao.getSymbols().map { flow -> flow.map { it.symbol } }

    override suspend fun isSymbolListEmpty(): Boolean = symbolDao.getSymbolListSize() == 0

    override suspend fun insertSymbols(symbols: List<String>): ErrorRetrieved? = tryCall {
        val dbSymbols: List<Symbol> = symbols.map { Symbol(symbol = it) }
        symbolDao.insertSymbols(dbSymbols)
    }.fold(
        ifLeft = { it },
        ifRight = { null }
    )

    override suspend fun deleteAllSymbols() = symbolDao.deleteAllSymbols()
}
