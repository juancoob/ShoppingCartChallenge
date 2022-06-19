package com.juancoob.shoppingcartchallenge.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juancoob.domain.Dorm
import com.juancoob.usecases.GetAvailableDormsUseCase
import com.juancoob.usecases.GetStoredDormsUseCase
import com.juancoob.usecases.InsertDormsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val insertDormUseCase: InsertDormsUseCase,
    private val getAvailableDormsUseCase: GetAvailableDormsUseCase,
    private val getStoredDormsUseCase: GetStoredDormsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    init {
        insertDorms()
        updateDormList()
    }

    private fun insertDorms() {
        viewModelScope.launch {
            if (getStoredDormsUseCase().isEmpty()) {
                val dorms: List<Dorm> = createDorms()
                insertDormUseCase(dorms)
            }
        }
    }

    private fun updateDormList() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            getAvailableDormsUseCase().collect { dorms ->
                _state.update { _state.value.copy(loading = false, dorms = dorms) }
            }
        }
    }

    private fun createDorms(): List<Dorm> {
        val dorms = mutableListOf<Dorm>()
        dorms.addAll(
            listOf(
                Dorm(
                    id = 0,
                    type = "6-bed dorm",
                    bedsAvailable = 6,
                    pricePerBed = 17.56,
                    currency = "USD",
                    currencySymbol = "$"
                ),
                Dorm(
                    id = 1,
                    type = "8-bed dorm",
                    bedsAvailable = 8,
                    pricePerBed = 14.50,
                    currency = "USD",
                    currencySymbol = "$"
                ),
                Dorm(
                    id = 2,
                    type = "12-bed dorm",
                    bedsAvailable = 12,
                    pricePerBed = 12.01,
                    currency = "USD",
                    currencySymbol = "$"
                )
            )
        )
        return dorms
    }

    data class UiState(
        val loading: Boolean = false,
        val dorms: List<Dorm>? = null
    )
}
