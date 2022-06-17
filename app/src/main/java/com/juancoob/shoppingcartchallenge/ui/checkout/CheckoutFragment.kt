package com.juancoob.shoppingcartchallenge.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.shoppingcartchallenge.R
import com.juancoob.shoppingcartchallenge.databinding.FragmentCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutFragment : Fragment(R.layout.fragment_checkout) {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val checkoutViewModel: CheckoutViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

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
                    updateCurrencySelector(it.symbolList)
                    updateCartList(it.cartUiStateList)
                    shouldShowErrorRetrieved(it.errorRetrieved?.errorToString())
                }
            }
        }
    }

    private fun shouldShowProgressBar(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun updateCurrencySelector(symbolList: List<String>?) {
        if (symbolList?.isNotEmpty() == true) {
            initCurrencySelector(symbolList)
        }
    }

    private fun initCurrencySelector(symbolList: List<String>) {
        val currencySelectorAdapter = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_spinner_item,
            symbolList
        )
        binding.availableCurrenciesSelector.adapter = currencySelectorAdapter
    }

    private fun updateCartList(cartUiStateList: List<CheckoutViewModel.CartUiState>?) {
        if (cartUiStateList != null) {
            cartAdapter.submitList(cartUiStateList)
        }
    }

    private fun shouldShowErrorRetrieved(errorRetrieved: String?) {
        binding.errorRetrieved.apply {
            text = errorRetrieved
            isVisible = errorRetrieved != null
        }
    }

    private fun ErrorRetrieved.errorToString(): String = when (this) {
        ErrorRetrieved.Connectivity -> getString(R.string.connectivity_error)
        is ErrorRetrieved.Server -> getString(R.string.server_error) + code
        is ErrorRetrieved.Unknown -> getString(R.string.unknown_error) + message
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
