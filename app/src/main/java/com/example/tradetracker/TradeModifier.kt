package com.example.tradetracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.navigation.fragment.findNavController
import com.example.tradetracker.databinding.TradeModifierBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TradeModifier : Fragment() {

    private var _binding: TradeModifierBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {

        _binding = TradeModifierBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun closeNewTrade() {
        binding.layoutTradeModifier.visibility = View.INVISIBLE
        clearTradeModifierEditTexts()
    }

    fun createNewTrade() {
        binding.layoutTradeModifier.visibility = View.INVISIBLE

        val trade: Trade = Trade(0, binding.edittextTradeModifierSymbol.text.toString(),
            binding.edittextTradeModifierEntry.text.toString().toDouble(),
            binding.edittextTradeModifierStopLoss.text.toString().toDouble(),
            binding.edittextTradeModifierTakeProfit.text.toString().toDouble(),
            binding.edittextTradeModifierShareQuantity.text.toString().toDouble())
        TradeManager().addToTradeList(trade)
        println(trade.toString())

        clearTradeModifierEditTexts()
    }

    private fun clearTradeModifierEditTexts() {
        binding.edittextTradeModifierSymbol.text.clear()
        binding.edittextTradeModifierEntry.text.clear()
        binding.edittextTradeModifierShareQuantity.text.clear()
        binding.edittextTradeModifierTakeProfit.text.clear()
        binding.edittextTradeModifierStopLoss.text.clear()
    }
}