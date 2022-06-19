package com.juancoob.shoppingcartchallenge.ui.detail

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
import com.juancoob.domain.Dorm
import com.juancoob.domain.ErrorRetrieved
import com.juancoob.shoppingcartchallenge.R
import com.juancoob.shoppingcartchallenge.databinding.FragmentDetailBinding
import com.juancoob.shoppingcartchallenge.ui.detail.DetailViewModel.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectStates(savedInstanceState)
        initListeners()
    }

    private fun collectStates(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.state.collect {
                    populateHeadline(it)
                    shouldInitSpinner(it.dorm, savedInstanceState?.getInt(SELECTOR_POSITION_KEY))
                }
            }
        }
    }

    private fun populateHeadline(uiState: UiState) {
        populateScreenTitle(uiState.dorm?.type)
        binding.availableBedsHeadline.text = when {
            uiState.dorm?.bedsAvailable == 0 -> getString(R.string.no_beds)
            uiState.dorm != null && uiState.dorm.bedsAvailable > 0 -> resources.getQuantityString(
                R.plurals.bedsAvailableFromDetailScreen,
                uiState.dorm.bedsAvailable,
                uiState.dorm.bedsAvailable,
                uiState.dorm.pricePerBed,
                uiState.dorm.currencySymbol
            )
            uiState.errorRetrieved != null -> uiState.errorRetrieved.errorToString()
            else -> ""
        }
        updateDetailScreenViews(
            uiState.dorm != null
                    && uiState.dorm.bedsAvailable > 0
                    && uiState.errorRetrieved == null
        )
    }

    private fun populateScreenTitle(title: String?) {
        binding.dormDetailToolbar.title = title
    }

    private fun updateDetailScreenViews(isVisible: Boolean) = binding.run {
        bedsSelector.isVisible = isVisible
        okButton.isVisible = isVisible
    }

    private fun ErrorRetrieved.errorToString(): String = when (this) {
        ErrorRetrieved.Connectivity -> getString(R.string.connectivity_error)
        is ErrorRetrieved.Server -> getString(R.string.server_error) + code
        is ErrorRetrieved.Unknown -> getString(R.string.unknown_error) + message
    }

    private fun shouldInitSpinner(dorm: Dorm?, position: Int?) {
        if (dorm != null) {
            initSpinner(dorm.bedsAvailable, position)
        }
    }

    private fun initSpinner(bedsAvailable: Int, previousPosition: Int?) {
        val adapter = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_spinner_item,
            bedsAvailableToList(bedsAvailable)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.bedsSelector.adapter = adapter
        val position = if (previousPosition == null || previousPosition > bedsAvailable) 0 else previousPosition
        binding.bedsSelector.setSelection(position)
    }

    private fun bedsAvailableToList(bedsAvailable: Int): List<Int> {
        val listOfBedsAvailable = mutableListOf<Int>()
        for (iterator: Int in 1..bedsAvailable) {
            listOfBedsAvailable.add(iterator)
        }
        return listOfBedsAvailable
    }

    private fun initListeners() {
        binding.okButton.setOnClickListener {
            val bookedBeds: Int = binding.bedsSelector.selectedItem as Int
            detailViewModel.addBookedBeds(bookedBeds)
        }
        binding.dormDetailToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTOR_POSITION_KEY, binding.bedsSelector.selectedItemPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SELECTOR_POSITION_KEY = "selectorPositionKey"
    }
}
