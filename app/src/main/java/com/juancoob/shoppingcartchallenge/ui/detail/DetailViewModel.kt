package com.juancoob.shoppingcartchallenge.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juancoob.domain.Bed
import com.juancoob.domain.Dorm
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.shoppingcartchallenge.data.toErrorRetrieved
import com.juancoob.shoppingcartchallenge.di.qualifiers.DormId
import com.juancoob.usecases.GetAvailableDormByIdUseCase
import com.juancoob.usecases.StoreAvailableBedForCheckoutUseCase
import com.juancoob.usecases.UpdateDormUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @DormId private val dormId: Int,
    private val getAvailableDormByIdUseCase: GetAvailableDormByIdUseCase,
    private val updateDormUseCase: UpdateDormUseCase,
    private val storeAvailableBedForCheckoutUseCase: StoreAvailableBedForCheckoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        getDorm()
    }

    private fun getDorm() {
        viewModelScope.launch {
            getAvailableDormByIdUseCase(dormId)
                .catch { cause -> _state.update { it.copy(errorRetrieved = cause.toErrorRetrieved()) } }
                .collect { dorm -> _state.update { UiState(dorm = dorm) } }
        }
    }

    fun addBookedBeds(bookedBeds: Int) {
        viewModelScope.launch {
            (1..bookedBeds).forEach { _ ->
                storeAvailableBedForCheckoutUseCase(
                    Bed(
                        _state.value.dorm!!.id,
                        _state.value.dorm!!.pricePerBed,
                        _state.value.dorm!!.currency
                    )
                )
            }
            updateDorms(bookedBeds)
        }
    }

    private suspend fun updateDorms(bookedBeds: Int) {
        updateDormUseCase(_state.value.dorm!!.copy(bedsAvailable = _state.value.dorm!!.bedsAvailable - bookedBeds))
    }

    data class UiState(
        val dorm: Dorm? = null,
        val errorRetrieved: ErrorRetrieved? = null
    )

}
