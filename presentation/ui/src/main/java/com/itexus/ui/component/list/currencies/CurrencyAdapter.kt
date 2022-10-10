package com.itexus.ui.component.list.currencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itexus.ui.databinding.ListItemCurrenciesBinding

class CurrencyAdapter :
    ListAdapter<CurrencyItemModel, CurrencyAdapter.ViewHolder>(CurrencyItemModel.diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ListItemCurrenciesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .let(::ViewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
    }

    class ViewHolder(private var itemBinding: ListItemCurrenciesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(item: CurrencyItemModel) {
            itemBinding.currencyValue.text = item.value.toString()
            itemBinding.currencyName.text = item.name
        }
    }
}
