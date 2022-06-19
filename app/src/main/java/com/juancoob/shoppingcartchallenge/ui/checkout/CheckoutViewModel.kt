package com.juancoob.shoppingcartchallenge.ui.checkout

import android.icu.util.Currency
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juancoob.domain.Bed
import com.juancoob.domain.Cart
import com.juancoob.domain.Dorm
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.shoppingcartchallenge.data.toErrorRetrieved
import com.juancoob.usecases.DeleteBedForCheckoutUseCase
import com.juancoob.usecases.GetCartUseCase
import com.juancoob.usecases.GetConversionUseCase
import com.juancoob.usecases.GetStoredDormsUseCase
import com.juancoob.usecases.GetSymbolsUseCase
import com.juancoob.usecases.InsertBedForCheckoutUseCase
import com.juancoob.usecases.RequestSymbolsUseCase
import com.juancoob.usecases.UpdateBedsUseCase
import com.juancoob.usecases.UpdateDormUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val requestSymbolsUseCase: RequestSymbolsUseCase,
    private val getSymbolsUseCase: GetSymbolsUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val insertBedForCheckoutUseCase: InsertBedForCheckoutUseCase,
    private val updateBedsUseCase: UpdateBedsUseCase,
    private val deleteBedForCheckoutUseCase: DeleteBedForCheckoutUseCase,
    private val getDormsUseCase: GetStoredDormsUseCase,
    private val updateDormUseCase: UpdateDormUseCase,
    private val getConversionUseCase: GetConversionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    init {
        start()
    }

    private fun start() {
        viewModelScope.launch {
            populateSymbols()
        }
        viewModelScope.launch {
            populateCartContent()
        }
    }

    private suspend fun populateSymbols() {
        _state.value = UiState(loading = true)
        val errorRetrieved = requestSymbolsUseCase()
        if (errorRetrieved != null) {
            _state.value =
                _state.value.copy(
                    loading = false,
                    errorRetrieved = errorRetrieved,
                    retry = ::start
                )
        } else {
            getSymbolsUseCase()
                .catch { cause ->
                    _state.update {
                        _state.value.copy(
                            loading = false,
                            errorRetrieved = cause.toErrorRetrieved(),
                            retry = ::start
                        )
                    }
                }
                .collect { symbols ->
                    _state.update {
                        _state.value.copy(
                            loading = it.cartUiStateList == null,
                            symbolList = symbols
                        )
                    }
                }
        }
    }

    private suspend fun populateCartContent() {
        if (_state.value.errorRetrieved != null) return
        getCartUseCase()
            .catch { cause ->
                _state.update {
                    _state.value.copy(
                        loading = false,
                        errorRetrieved = cause.toErrorRetrieved(),
                        retry = ::start
                    )
                }
            }
            .collect { cartList ->
                _state.update {
                    _state.value.copy(
                        loading = it.symbolList == null || it.symbolList.isEmpty(),
                        cartUiStateList = cartList.map { cartElement -> cartElement.toUiState() }
                    )
                }
            }
    }

    private fun Cart.toUiState() = CartUiState(
        cart = this,
        amountPerDorm = pricePerBed * bedsForCheckout,
        onAddBed = {
            if (bedsAvailable > 0) {
                addBookedBedsFromDorm(this)
            }
        },
        onSubtractBed = {
            if (bedsForCheckout > 0) {
                subtractBookedBedsFromDorm(this, 1)
            }
        },
        onDeleteCartItem = {
            subtractBookedBedsFromDorm(this, bedsForCheckout)
        }
    )

    private fun addBookedBedsFromDorm(cart: Cart) {
        viewModelScope.launch {
            val dorm = getDormFromCart(cart)
            storeBookedBedsFromDorm(dorm)
            updateDormBookedBeds(dorm, dorm.bedsAvailable - 1)
        }
    }

    private fun getDormFromCart(cart: Cart): Dorm = cart.run {
        Dorm(
            id = dormId,
            type = type,
            bedsAvailable = bedsAvailable,
            currency = currency,
            pricePerBed = pricePerBed,
            currencySymbol = currencySymbol
        )
    }

    private suspend fun storeBookedBedsFromDorm(dorm: Dorm) = dorm.run {
        val errorRetrieved = insertBedForCheckoutUseCase(
            Bed(
                dormId = id,
                pricePerBed = pricePerBed,
                currency = currency,
                currencySymbol = currencySymbol
            )
        )
        if (errorRetrieved != null) {
            _state.update { it.copy(errorRetrieved = errorRetrieved) }
        }
    }

    private suspend fun updateDormBookedBeds(dorm: Dorm, currentBookedBeds: Int) {
        val errorRetrieved = updateDormUseCase(dorm.copy(bedsAvailable = currentBookedBeds))
        if (errorRetrieved != null) {
            _state.update { it.copy(errorRetrieved = errorRetrieved) }
        }
    }

    private fun subtractBookedBedsFromDorm(cart: Cart, bookedBeds: Int) {
        viewModelScope.launch {
            val dorm = getDormFromCart(cart)
            deleteBookedBedsFromDorm(dorm, bookedBeds)
            updateDormBookedBeds(dorm, dorm.bedsAvailable + bookedBeds)
        }
    }

    private suspend fun deleteBookedBedsFromDorm(dorm: Dorm, bookedBeds: Int) {
        var errorRetrieved: ErrorRetrieved?
        (1..bookedBeds).forEach { _ ->
            errorRetrieved = deleteBedForCheckoutUseCase(dorm.id)
            if (errorRetrieved != null) {
                _state.update { it.copy(errorRetrieved = errorRetrieved) }
                return@forEach
            }
        }
    }

    fun requestConversion(requestedCurrency: String) {
        var errorRetrieved: ErrorRetrieved?
        val newCurrencyCode = getCurrencyCode(requestedCurrency)
        val newCurrencySymbol = getCurrencySymbol(newCurrencyCode)
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val dorms = getDormsUseCase()
            dorms.forEach { dorm ->
                getConversionUseCase(
                    dorm.currency,
                    newCurrencyCode,
                    dorm.pricePerBed
                ).fold(ifLeft = { errorRetrieved ->
                    _state.update {
                        _state.value.copy(
                            loading = false,
                            errorRetrieved = errorRetrieved,
                            retry = ::start
                        )
                    }
                }) { updatedPricePerBed ->
                    errorRetrieved = updateDormUseCase(
                        dorm.copy(
                            pricePerBed = updatedPricePerBed,
                            currency = newCurrencyCode,
                            currencySymbol = newCurrencySymbol
                        )
                    )
                    if (errorRetrieved != null) {
                        _state.update { it.copy(errorRetrieved = errorRetrieved) }
                        return@forEach
                    }

                    errorRetrieved = updateBedsUseCase(
                        dormId = dorm.id,
                        pricePerBed = updatedPricePerBed,
                        currency = newCurrencyCode,
                        currencySymbol = newCurrencySymbol
                    )
                    if (errorRetrieved != null) {
                        _state.update { it.copy(errorRetrieved = errorRetrieved) }
                        return@forEach
                    }
                }
            }
            _state.value = _state.value.copy(loading = false)
        }
    }

    private fun getCurrencyCode(currency: String) = currency.take(CURRENCY_CODE_LENGTH)

    private fun getCurrencySymbol(currency: String) = Currency.getInstance(currency).symbol

    data class UiState(
        val loading: Boolean = false,
        val symbolList: List<String>? = null,
        val cartUiStateList: List<CartUiState>? = null,
        val errorRetrieved: ErrorRetrieved? = null,
        val retry: (() -> Unit)? = null
    )

    data class CartUiState(
        val cart: Cart,
        val amountPerDorm: Double,
        val onAddBed: () -> Unit,
        val onSubtractBed: () -> Unit,
        val onDeleteCartItem: () -> Unit
    )

    companion object {
        private const val CURRENCY_CODE_LENGTH = 3
    }
}
