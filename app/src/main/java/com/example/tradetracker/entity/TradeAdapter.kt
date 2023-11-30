package com.example.tradetracker.entity

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tradetracker.R
import com.example.tradetracker.entity.Trade

class TradeAdapter(context: Context, items: List<Trade>) : ArrayAdapter<Trade>(
    context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        val trade = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        var currView = convertView
        if (currView == null) {
            currView = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false)
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

        //Set colour of current price TextView
        if(trade!!.lastPrice!! < trade.buyPrice!!)
            tvPrice.setTextColor(context.resources.getColor(R.color.red_text, context.resources.newTheme()))
        else if(trade!!.lastPrice == trade!!.buyPrice)
            tvPrice.setTextColor(context.resources.getColor(R.color.white, context.resources.newTheme()))
        else
            tvPrice.setTextColor(context.resources.getColor(R.color.green_text, context.resources.newTheme()))


        // Return the completed view to render on screen
        return currView
    }

}