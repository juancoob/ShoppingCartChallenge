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
        collectStates()
        initListeners()
    }

    private fun collectStates() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.state.collect {
                    populateHeadline(it)
                    if (it.dorm != null) {
                        initSpinner(it.dorm.bedsAvailable)
                    }
                }
            }
        }
    }

    private fun populateHeadline(uiState: UiState) {
        if (uiState.dorm?.bedsAvailable == 0) {
            binding.availableBedsHeadline.text = getString(R.string.no_beds)
            hideViews()
        } else if (uiState.dorm != null) {
            binding.availableBedsHeadline.text = requireContext().resources.getQuantityString(
                R.plurals.bedsAvailableFromDetailScreen,
                uiState.dorm.bedsAvailable,
                uiState.dorm.bedsAvailable
            )
        } else if (uiState.errorRetrieved != null) {
            binding.availableBedsHeadline.text = uiState.errorRetrieved.errorToString()
        }
    }

    private fun hideViews() = binding.run {
        bedsSelector.isVisible = false
        okButton.isVisible = false
    }

    private fun ErrorRetrieved.errorToString(): String = when (this) {
        ErrorRetrieved.Connectivity -> getString(R.string.connectivity_error)
        is ErrorRetrieved.Server -> getString(R.string.server_error) + code
        is ErrorRetrieved.Unknown -> getString(R.string.unknown_error) + message
    }

    private fun initSpinner(bedsAvailable: Int) {
        val adapter = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_spinner_item,
            bedsAvailableToList(bedsAvailable)
        )
        binding.bedsSelector.adapter = adapter
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
