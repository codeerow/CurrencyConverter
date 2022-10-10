package com.itexus.ui.component.list.currencies

import androidx.recyclerview.widget.DiffUtil

data class CurrencyItemModel(
    val name: String,
    val value: Double
) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CurrencyItemModel>() {

            override fun areItemsTheSame(
                oldItem: CurrencyItemModel,
                newItem: CurrencyItemModel,
            ): Boolean {
                return oldItem.name == newItem.name && oldItem.value == newItem.value
            }

            override fun areContentsTheSame(
                oldItem: CurrencyItemModel,
                newItem: CurrencyItemModel,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
