package com.example.tradetracker.layout

import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tradetracker.R
import com.example.tradetracker.entity.Trade
import com.example.tradetracker.entity.TradeManager
import com.example.tradetracker.model.TradeViewModel
import java.lang.NumberFormatException

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TradeModifier(activity: AppCompatActivity) {

    private var mTradeViewModel: TradeViewModel = ViewModelProvider(activity)[TradeViewModel::class.java]

    private val layoutTradeModifier = activity.findViewById<RelativeLayout>(R.id.layout_trade_modifier)
    private val edittextTradeModifierSymbol = activity.findViewById<EditText>(R.id.edittext_trade_modifier_symbol)
    private val edittextTradeModifierEntry = activity.findViewById<EditText>(R.id.edittext_trade_modifier_entry)
    private val edittextTradeModifierStopLoss = activity.findViewById<EditText>(R.id.edittext_trade_modifier_stop_loss)
    private val edittextTradeModifierTakeProfit = activity.findViewById<EditText>(R.id.edittext_trade_modifier_take_profit)
    private val edittextTradeModifierShareValue = activity.findViewById<EditText>(R.id.edittext_trade_modifier_share_value)

    fun closeNewTrade() {        layoutTradeModifier.visibility = View.INVISIBLE
        clearTradeModifierEditTexts()
    }

    fun createNewTrade() {
        layoutTradeModifier.visibility = View.INVISIBLE

        try {
//            val trade: Trade = Trade(0, edittextTradeModifierSymbol.text.toString(),
//                edittextTradeModifierEntry.text.toString().toDouble(),
//                edittextTradeModifierStopLoss.text.toString().toDouble(),
//                edittextTradeModifierTakeProfit.text.toString().toDouble(),
//                edittextTradeModifierShareValue.text.toString().toDouble())
//            TradeManager().addToTradeList(trade)
            mTradeViewModel.insert(edittextTradeModifierSymbol.text.toString(),
                edittextTradeModifierEntry.text.toString().toDouble(),
                edittextTradeModifierStopLoss.text.toString().toDouble(),
                edittextTradeModifierTakeProfit.text.toString().toDouble(),
                edittextTradeModifierShareValue.text.toString().toDouble())
        } catch(e: NumberFormatException) {
            Log.i("Trade Modifier", "Empty EditText")
        }

        clearTradeModifierEditTexts()
    }

    private fun clearTradeModifierEditTexts() {
        edittextTradeModifierSymbol.text.clear()
        edittextTradeModifierEntry.text.clear()
        edittextTradeModifierShareValue.text.clear()
        edittextTradeModifierTakeProfit.text.clear()
        edittextTradeModifierStopLoss.text.clear()
    }
}