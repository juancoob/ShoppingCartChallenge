package com.juancoob.shoppingcartchallenge.ui.common

import android.content.Context
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.shoppingcartchallenge.R


fun ErrorRetrieved.errorToString(context: Context): String = when (this) {
    ErrorRetrieved.Connectivity -> context.getString(R.string.connectivity_error)
    is ErrorRetrieved.Server -> context.getString(R.string.server_error) + code
    is ErrorRetrieved.Unknown -> context.getString(R.string.unknown_error) + message
}
