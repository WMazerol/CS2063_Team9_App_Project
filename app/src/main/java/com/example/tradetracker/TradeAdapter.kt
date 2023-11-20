package com.example.tradetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TradeAdapter(context: Context, items: List<Trade>) : ArrayAdapter<Trade>(
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

        // TODO
        //  Set the text used by tvName and tvNum using the data object
        //  This will need to updated once the entity model has been updated
        tvSymbol.text = trade!!.getSymbol()
        tvPrice.text = trade!!.getLastPrice().toString()
        tvBuyPrice.text = trade!!.getBuyPrice().toString()
        tvStopLoss.text = trade!!.getStopLoss().toString()
        tvTakeProfit.text = trade!!.getTakeProfit().toString()

        // Return the completed view to render on screen
        return currView
    }
}