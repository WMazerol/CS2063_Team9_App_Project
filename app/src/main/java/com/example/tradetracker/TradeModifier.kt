package com.example.tradetracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.tradetracker.databinding.TradeModifierBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TradeModifier : AppCompatActivity() {

    private var _binding: TradeModifierBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(
        //inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ) {
        super.onCreate(savedInstanceState)

        _binding = TradeModifierBinding.inflate(layoutInflater)

    }

//    override fun onViewCreated(
//        //view: View,
//        savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }

    override fun onDestroy() {
        super.onDestroy()
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
            binding.edittextTradeModifierShareValue.text.toString().toDouble())
        TradeManager().addToTradeList(trade)
        println(trade.toString())

        clearTradeModifierEditTexts()
    }

    private fun clearTradeModifierEditTexts() {
        binding.edittextTradeModifierSymbol.text.clear()
        binding.edittextTradeModifierEntry.text.clear()
        binding.edittextTradeModifierShareValue.text.clear()
        binding.edittextTradeModifierTakeProfit.text.clear()
        binding.edittextTradeModifierStopLoss.text.clear()
    }
}