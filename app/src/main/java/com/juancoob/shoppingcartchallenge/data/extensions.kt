package com.juancoob.shoppingcartchallenge.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.io.IOException
import com.juancoob.domain.ErrorRetrieved
import retrofit2.HttpException

suspend fun <T> tryCall(action: suspend () -> T): Either<ErrorRetrieved, T> = try {
    action().right()
} catch (e: Exception) {
    e.toErrorRetrieved().left()
}

fun Throwable.toErrorRetrieved(): ErrorRetrieved = when(this) {
    is IOException -> ErrorRetrieved.Connectivity
    is HttpException -> ErrorRetrieved.Server(code())
    else -> ErrorRetrieved.Unknown(message ?: "")
}
