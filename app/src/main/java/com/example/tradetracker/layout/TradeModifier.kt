package com.example.tradetracker.layout

import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tradetracker.APIController
import com.example.tradetracker.KeyboardUtils
import com.example.tradetracker.MainActivity
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
    private val apiController = APIController(MainActivity())

    private val layoutTradeModifier = activity.findViewById<RelativeLayout>(R.id.layout_trade_modifier)
    private val edittextTradeModifierSymbol = activity.findViewById<EditText>(R.id.edittext_trade_modifier_symbol)
    private val edittextTradeModifierEntry = activity.findViewById<EditText>(R.id.edittext_trade_modifier_entry)
    private val edittextTradeModifierStopLoss = activity.findViewById<EditText>(R.id.edittext_trade_modifier_stop_loss)
    private val edittextTradeModifierTakeProfit = activity.findViewById<EditText>(R.id.edittext_trade_modifier_take_profit)
    private val edittextTradeModifierShareValue = activity.findViewById<EditText>(R.id.edittext_trade_modifier_share_value)

    private val textviewTradeModifierInvalidSymbol = activity.findViewById<TextView>(R.id.textview_trade_modifier_invalid_symbol)

    fun backFromTrade() {
        layoutTradeModifier.visibility = View.INVISIBLE
        clearTradeModifierEditTexts()
    }

    fun createNewTrade() {
        if(edittextTradeModifierSymbol.text.toString() != "" &&
            apiController.checkSymbolIsValid(edittextTradeModifierSymbol.text.toString())) {
            try {
                mTradeViewModel.insert(
                    edittextTradeModifierSymbol.text.toString(),
                    edittextTradeModifierEntry.text.toString().toDouble(),
                    edittextTradeModifierStopLoss.text.toString().toDouble(),
                    edittextTradeModifierTakeProfit.text.toString().toDouble(),
                    edittextTradeModifierShareValue.text.toString().toDouble()
                )

                clearTradeModifierEditTexts()
                layoutTradeModifier.visibility = View.INVISIBLE
            } catch (e: NumberFormatException) {
                Log.i("Trade Modifier", "Empty EditText")
            }
        } else {
            textviewTradeModifierInvalidSymbol.visibility = View.VISIBLE
        }
    }

    fun saveModifiedTrade(trade: Trade) {
        trade!!.buyPrice = edittextTradeModifierEntry.text.toString().toDouble()
        trade!!.stopLoss = edittextTradeModifierStopLoss.text.toString().toDouble()
        trade!!.takeProfit = edittextTradeModifierTakeProfit.text.toString().toDouble()
        trade!!.shareValue = edittextTradeModifierShareValue.text.toString().toDouble()

        mTradeViewModel.update(trade!!)
        Log.i("Trade Modifier", "ID: "+trade!!.id)

        clearTradeModifierEditTexts()
        layoutTradeModifier.visibility = View.INVISIBLE
    }

    fun deleteTrade(trade: Trade) {
        mTradeViewModel.delete(trade)

        clearTradeModifierEditTexts()
        layoutTradeModifier.visibility = View.INVISIBLE
    }

    fun populateEdittextsFromTrade(trade: Trade) {
        edittextTradeModifierSymbol.setText(trade.symbol)
        edittextTradeModifierEntry.setText(trade.buyPrice.toString())
        edittextTradeModifierStopLoss.setText(trade.stopLoss.toString())
        edittextTradeModifierTakeProfit.setText(trade.takeProfit.toString())
        edittextTradeModifierShareValue.setText(trade.shareValue.toString())

        edittextTradeModifierSymbol.isEnabled = false
    }

    fun closeExistingTrade(trade: Trade) {
        mTradeViewModel.closeTrade(trade)
    }

    private fun clearTradeModifierEditTexts() {
        textviewTradeModifierInvalidSymbol.visibility = View.INVISIBLE
        edittextTradeModifierSymbol.text.clear()
        edittextTradeModifierEntry.text.clear()
        edittextTradeModifierShareValue.text.clear()
        edittextTradeModifierTakeProfit.text.clear()
        edittextTradeModifierStopLoss.text.clear()
        edittextTradeModifierSymbol.isEnabled = true
    }
}