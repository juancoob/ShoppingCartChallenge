package com.juancoob.shoppingcartchallenge.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.juancoob.domain.Dorm
import com.juancoob.shoppingcartchallenge.R
import com.juancoob.shoppingcartchallenge.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var adapter: DormAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        collectStates()
    }

    private fun initList() {
        adapter = DormAdapter(::goToDetailFragment)
        binding.mainList.adapter = adapter
    }

    private fun collectStates() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.state.collect {
                    binding.progressBar.isVisible = it.loading
                    binding.noDorms.isVisible = it.dorms?.isEmpty() ?: false
                    adapter.submitList(it.dorms)
                }
            }
        }
    }

    private fun goToDetailFragment(dorm: Dorm) {
        val action = MainFragmentDirections.actionMainToDetail(dorm.id)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
