package com.juancoob.shoppingcartchallenge.ui.checkout

import android.graphics.Color
import android.icu.util.Currency
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.juancoob.shoppingcartchallenge.R
import com.juancoob.shoppingcartchallenge.databinding.FragmentCheckoutBinding
import com.juancoob.shoppingcartchallenge.ui.common.errorToString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val checkoutViewModel: CheckoutViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter
    private var currencySelectorAdapter: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        collectStates()
    }

    private fun initList() {
        cartAdapter = CartAdapter()
        binding.checkoutList.adapter = cartAdapter
    }

    private fun collectStates() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.state.collect {
                    shouldShowProgressBar(it.loading)
                    shouldInitCurrencySelector(it.symbolList)
                    shouldSetCurrentCurrencyPositionOnSelector(it.symbolList, it.cartUiStateList)
                    cartAdapter.submitList(it.cartUiStateList)
                    updateCheckoutScreenViews(it.cartUiStateList)
                    shouldOnlyShowErrorRetrieved(
                        it.errorRetrieved?.errorToString(binding.root.context),
                        it.retry
                    )
                }
            }
        }
    }

    private fun shouldShowProgressBar(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun shouldInitCurrencySelector(symbolList: List<String>?) {
        if (symbolList?.isNotEmpty() == true && currencySelectorAdapter == null) {
            initCurrencySelector(symbolList)
        }
    }

    private fun initCurrencySelector(symbolList: List<String>) {
        currencySelectorAdapter = initSpinnerAdapter(symbolList)
        currencySelectorAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.availableCurrenciesSelector.adapter = currencySelectorAdapter
    }

    private fun initSpinnerAdapter(symbolList: List<String>): ArrayAdapter<String> =
        object : ArrayAdapter<String>(
            binding.root.context,
            android.R.layout.simple_spinner_item,
            symbolList
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also { view ->
                    if (position == binding.availableCurrenciesSelector.selectedItemPosition) {
                        view.setBackgroundColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.purple_200
                            )
                        )
                    } else {
                        view.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
        }

    private fun shouldSetCurrentCurrencyPositionOnSelector(
        symbolList: List<String>?,
        cartUiStateList: List<CheckoutViewModel.CartUiState>?
    ) {
        if (symbolList?.isNotEmpty() == true
            && cartUiStateList?.isNotEmpty() == true
            && binding.availableCurrenciesSelector.onItemSelectedListener == null
        ) {
            val currentCurrency = cartUiStateList.map { it.cart.currency }.first()
            setCurrentCurrencyPositionOnSelector(symbolList, currentCurrency)
        }
    }

    private fun setCurrentCurrencyPositionOnSelector(
        symbolList: List<String>,
        currentCurrency: String
    ) {
        symbolList.first { it.contains(currentCurrency) }.also {
            val currentCurrencyPosition = symbolList.indexOf(it)
            binding.availableCurrenciesSelector.setSelection(currentCurrencyPosition, false)
            setCurrencyPositionListener()
        }
    }

    private fun setCurrencyPositionListener() {
        binding.availableCurrenciesSelector.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    integer: Int,
                    long: Long
                ) {
                    val selectedCurrency =
                        (binding.availableCurrenciesSelector.selectedItem as String).take(CURRENCY_CODE_LENGTH)
                    checkoutViewModel.requestConversion(
                        selectedCurrency,
                        Currency.getInstance(selectedCurrency).symbol
                    )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    return
                }
            }
    }

    private fun updateCheckoutScreenViews(cartUiStateList: List<CheckoutViewModel.CartUiState>?) =
        binding.run {
            emptyCart.isVisible = cartUiStateList == null || cartUiStateList.isEmpty()
            amountInLabel.isVisible = cartUiStateList?.isNotEmpty() == true
            availableCurrenciesSelector.isVisible = cartUiStateList?.isNotEmpty() == true
            checkoutList.isVisible = cartUiStateList?.isNotEmpty() == true
            totalAmount.apply {
                isVisible = cartUiStateList?.isNotEmpty() == true
                if (isVisible) {
                    text = getString(
                        R.string.total_amount,
                        cartUiStateList!!.sumOf { it.amountPerDorm },
                        cartUiStateList.map { it.cart.currencySymbol }.first()
                    )
                }
            }
        }

    private fun shouldOnlyShowErrorRetrieved(errorMessage: String?, retry: (() -> Unit)?) =
        binding.run {
            if (emptyCart.isVisible) {
                emptyCart.isVisible = errorMessage == null
            } else {
                amountInLabel.isVisible = errorMessage == null
                availableCurrenciesSelector.isVisible = errorMessage == null
                checkoutList.isVisible = errorMessage == null
                totalAmount.isVisible = errorMessage == null
            }
            errorRetrieved.apply {
                text = errorMessage
                isVisible = errorMessage != null
            }
            retryButton.apply {
                isVisible = errorMessage != null
                setOnClickListener {
                    if (isVisible && retry != null) {
                        retry()
                    }
                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val CURRENCY_CODE_LENGTH = 3
    }
}
