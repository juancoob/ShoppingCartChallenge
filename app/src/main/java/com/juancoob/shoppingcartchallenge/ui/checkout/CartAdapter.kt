package com.juancoob.shoppingcartchallenge.ui.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juancoob.domain.Cart
import com.juancoob.shoppingcartchallenge.R
import com.juancoob.shoppingcartchallenge.databinding.CartItemBinding
import com.juancoob.shoppingcartchallenge.ui.checkout.CheckoutViewModel.CartUiState

class CartAdapter :
    ListAdapter<CartUiState, CartAdapter.ViewHolder>(object : DiffUtil.ItemCallback<CartUiState>() {
        override fun areItemsTheSame(oldItem: CartUiState, newItem: CartUiState): Boolean =
            oldItem.cart.dormId == newItem.cart.dormId

        override fun areContentsTheSame(oldItem: CartUiState, newItem: CartUiState): Boolean =
            oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartUiState = getItem(position)
        holder.bind(cartUiState)
    }

    inner class ViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartUiState: CartUiState) {
            setBookedRooms(cartUiState.cart.bedsForCheckout)
            showBedTypeCostText(cartUiState.cart)
            initClickListeners(cartUiState)
        }

        private fun setBookedRooms(bedsForCheckout: Int) {
            binding.bedsBooked.text = bedsForCheckout.toString()
        }

        private fun showBedTypeCostText(cart: Cart) {
            binding.cartElementText.text = binding.root.context.resources.getQuantityString(
                R.plurals.bedsBookedFromCheckoutScreen,
                cart.bedsForCheckout,
                cart.type,
                cart.pricePerBed * cart.bedsForCheckout,
                cart.currencySymbol
            )
        }

        private fun initClickListeners(cartUiState: CartUiState) {
            binding.run {
                addBed.setOnClickListener {
                    cartUiState.onAddBed()
                }
                subtractBed.setOnClickListener {
                    cartUiState.onSubtractBed()
                }
                deleteCartItem.setOnClickListener {
                    cartUiState.onDeleteCartItem()
                }
            }
        }
    }
}
