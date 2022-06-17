package com.juancoob.shoppingcartchallenge.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juancoob.domain.Bed
import com.juancoob.domain.Cart
import com.juancoob.domain.Dorm
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.shoppingcartchallenge.data.toErrorRetrieved
import com.juancoob.usecases.DeleteAStoredBedForCheckoutUseCase
import com.juancoob.usecases.DeleteSymbolsUseCase
import com.juancoob.usecases.GetCartUseCase
import com.juancoob.usecases.GetConversionUseCase
import com.juancoob.usecases.GetSymbolsUseCase
import com.juancoob.usecases.RequestSymbolsUseCase
import com.juancoob.usecases.StoreAvailableBedForCheckoutUseCase
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
    private val storeAvailableBedForCheckoutUseCase: StoreAvailableBedForCheckoutUseCase,
    private val deleteAStoredBedForCheckoutUseCase: DeleteAStoredBedForCheckoutUseCase,
    private val updateDormUseCase: UpdateDormUseCase,
    private val getConversionUseCase: GetConversionUseCase,
    private val deleteSymbolsUseCase: DeleteSymbolsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            populateSymbols()
            populateCartContent()
        }
    }

    private suspend fun populateSymbols() {
        _state.value = UiState(loading = true)
        val errorRetrieved = requestSymbolsUseCase()
        if (errorRetrieved != null) {
            _state.value = _state.value.copy(loading = false, errorRetrieved = errorRetrieved)
        } else {
            getSymbolsUseCase()
                .catch { cause ->
                    _state.update {
                        _state.value.copy(
                            loading = false,
                            errorRetrieved = cause.toErrorRetrieved()
                        )
                    }
                }
                .collect { symbols ->
                    _state.update {
                        _state.value.copy(
                            loading = false,
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
                        errorRetrieved = cause.toErrorRetrieved()
                    )
                }
            }
            .collect { cartList ->
                _state.update {
                    _state.value.copy(
                        loading = false,
                        cartUiStateList = cartList.map { it.toUiState() }
                    )
                }
            }
    }

    private fun Cart.toUiState() = CartUiState(
        this,
        onAddBed = {
            addBookedBedsFromDorm(this)
        },
        onSubtractBed = {
            subtractBookedBedsFromDorm(this, 1)
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
            maxBeds = bedsAvailable + bedsForCheckout
        )
    }

    private suspend fun storeBookedBedsFromDorm(dorm: Dorm) {
        storeAvailableBedForCheckoutUseCase(
            Bed(
                dorm.id,
                dorm.pricePerBed,
                dorm.currency
            )
        )
    }

    private suspend fun updateDormBookedBeds(dorm: Dorm, currentBookedBeds: Int) {
        updateDormUseCase(dorm.copy(bedsAvailable = currentBookedBeds))
    }

    private fun subtractBookedBedsFromDorm(cart: Cart, bookedBeds: Int) {
        viewModelScope.launch {
            val dorm = getDormFromCart(cart)
            deleteBookedBedsFromDorm(dorm, bookedBeds)
            updateDormBookedBeds(dorm, dorm.bedsAvailable + bookedBeds)
        }
    }

    private suspend fun deleteBookedBedsFromDorm(dorm: Dorm, bookedBeds: Int) {
        (1..bookedBeds).forEach { _ ->
            deleteAStoredBedForCheckoutUseCase(
                Bed(
                    dorm.id,
                    dorm.pricePerBed,
                    dorm.currency
                )
            )
        }
    }

    fun requestConversion(cartList: List<Cart>, requestedCurrency: String) {
        viewModelScope.launch {
            val updatedCartList = mutableListOf<Cart>()
            _state.value = _state.value.copy(loading = true)
            cartList.forEach { cartElement ->
                getConversionUseCase(
                    cartElement.currency,
                    requestedCurrency,
                    cartElement.pricePerBed
                ).fold(ifLeft = { errorRetrieved ->
                    _state.update {
                        _state.value.copy(
                            loading = false,
                            errorRetrieved = errorRetrieved
                        )
                    }
                }) { updatedPricePerBed ->
                    updatedCartList.add(
                        cartElement.copy(
                            pricePerBed = updatedPricePerBed,
                            currency = requestedCurrency
                        )
                    )
                }
            }
            _state.value = _state.value.copy(loading = false)
            if (updatedCartList.isNotEmpty()) {
                _state.update { _state.value.copy(cartUiStateList = updatedCartList.map { it.toUiState() }) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            deleteSymbolsUseCase()
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val symbolList: List<String>? = null,
        val cartUiStateList: List<CartUiState>? = null,
        val errorRetrieved: ErrorRetrieved? = null
    )

    data class CartUiState(
        val cart: Cart,
        val onAddBed: () -> Unit,
        val onSubtractBed: () -> Unit,
        val onDeleteCartItem: () -> Unit
    )
}
