package com.itexus.ui.screen.currencyconverter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itexus.ui.R
import com.itexus.ui.databinding.ScreenCurrencyConverterBinding
import com.itexus.ui.util.flow.bind
import com.itexus.ui.component.list.currencies.CurrencyAdapter
import com.itexus.ui.screen.currencyconverter.dialog.ExchangeCompleteDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyConverterScreen : Fragment(R.layout.screen_currency_converter) {

    private val binding by viewBinding(ScreenCurrencyConverterBinding::bind)
    private val viewModel: CurrencyConverterViewModelApi by viewModel()

    private val currencyAdapter = CurrencyAdapter()
    private var toast: Toast? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        renderView()
    }

    private fun bindViewModel() = with(binding) {
        viewModel.balances.bind(viewLifecycleOwner, onNext = currencyAdapter::submitList)
        viewModel.receivedAmount.bind(viewLifecycleOwner, onNext = receivedValue::setText)
        viewModel.isSubmitAvailable.bind(viewLifecycleOwner, onNext = submitButton::setEnabled)
        viewModel.transactionMessage.bind(viewLifecycleOwner, onNext = ::showTransactionInfo)
        viewModel.error.bind(viewLifecycleOwner) {
            toast?.cancel()
            toast = Toast.makeText(requireContext(), it, Toast.LENGTH_LONG)
            toast?.show()
        }

        viewModel.ratesList.bind(viewLifecycleOwner, onNext = {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it,
            )
            currencySell.adapter = adapter
            currencySell.onItemSelectedListener =
                buildOnItemSelectedListener { position -> viewModel.onFromChanged(it[position]) }

            currencyReceive.adapter = adapter
            currencyReceive.onItemSelectedListener =
                buildOnItemSelectedListener { position -> viewModel.onToChanged(it[position]) }
        })
    }

    private fun renderView() = with(binding) {
        balanceList.adapter = currencyAdapter
        editTextSell.addTextChangedListener { sellCurrency ->
            val value = sellCurrency.toString()
            viewModel.onSellAmountChanged(value)
        }
        submitButton.setOnClickListener { viewModel.submit() }
    }

    private fun showTransactionInfo(text: String) {
        val dialogFragment = ExchangeCompleteDialogFragment(text)
        dialogFragment.show(childFragmentManager, "dialog")
    }

    private fun buildOnItemSelectedListener(block: (Int) -> Unit): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                block(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}
