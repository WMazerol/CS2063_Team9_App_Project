package com.example.tradetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tradetracker.entity.TradeEntity

class TradeAdapter(context: Context, items: List<TradeEntity>) : ArrayAdapter<TradeEntity>(
    context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        val trade = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        var currView = convertView
        if (currView == null) {
            currView = LayoutInflater.from(context).inflate(R.layout.fragment_first, parent, false)
        }

        // Lookup view for data population
        val tvSymbol = currView!!.findViewById<TextView>(R.id.textViewSymbol)
        val tvPrice = currView.findViewById<TextView>(R.id.textViewPrice)
        val tvBuyPrice = currView.findViewById<TextView>(R.id.textViewBuyPrice)
        val tvStopLoss = currView.findViewById<TextView>(R.id.textViewStopLoss)
        val tvTakeProfit = currView.findViewById<TextView>(R.id.textViewTakeProfit)

        tvSymbol.text = trade!!.symbol
        tvPrice.text = trade!!.lastPrice.toString()
        tvBuyPrice.text = trade!!.buyPrice.toString()
        tvStopLoss.text = trade!!.stopLoss.toString()
        tvTakeProfit.text = trade!!.takeProfit.toString()

        // Return the completed view to render on screen
        return currView
    }
}