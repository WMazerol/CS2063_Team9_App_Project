package com.example.tradetracker.entity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.daimajia.swipe.SwipeLayout
import com.example.tradetracker.MainActivity
import com.example.tradetracker.R
import com.example.tradetracker.entity.Trade
import com.example.tradetracker.layout.TradeModifier
import com.example.tradetracker.model.TradeViewModel
import com.example.tradetracker.repository.TradeRepository
import kotlinx.coroutines.MainScope

class TradeAdapter(context: Context, items: List<Trade>) : ArrayAdapter<Trade>(
    context, 0, items) {

    private val tradeRepository: TradeRepository = TradeRepository(context.applicationContext as Application)
    private var swipeOpenStatuses: ArrayList<SwipeLayout.Status> = ArrayList<SwipeLayout.Status>(0)

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
        val tvShareValue = currView.findViewById<TextView>(R.id.textViewAmount)
        val bSwipeCloseTrade = currView.findViewById<Button>(R.id.buttonSwipeCloseTrade)

        tvSymbol.text = trade!!.symbol
        tvBuyPrice.text = trade.buyPrice.toString()
        tvStopLoss.text = trade.stopLoss.toString()
        tvTakeProfit.text = trade.takeProfit.toString()
        tvShareValue.text = trade.shareValue.toString()

        try {
            updateCurrentPriceTextView(currView, trade.lastPrice!!, trade.buyPrice!!)
        } catch (e: NullPointerException) {}

        if(trade.closingDate == null) {
            bSwipeCloseTrade.setOnClickListener {
                tradeRepository.closeTrade(trade)
            }
            run{setupListItemSwipe(currView, trade)}
        } else {
            currView.findViewById<SwipeLayout>(R.id.swipe_list_view).isSwipeEnabled = false
        }


        // Return the completed view to render on screen
        return currView
    }


    private fun setupListItemSwipe(view: View, trade: Trade) {
        val swipeView: SwipeLayout = view.findViewById(R.id.swipe_list_view)

        var swipeViewOpenStatus = swipeView.openStatus

        swipeView.showMode = SwipeLayout.ShowMode.PullOut
        swipeView.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper))

        swipeView.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onClose(layout: SwipeLayout) {
                //Log.i("Swipe List Item", "onClose")
                swipeView.isClickable = false
            }

            override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {
                //Log.i("Swipe List Item", "swiping")
            }

            override fun onStartOpen(layout: SwipeLayout) {
                //Log.i("Swipe List Item", "on start open")
                swipeView.isClickable = false
            }

            override fun onOpen(layout: SwipeLayout) {
                //Log.i("Swipe List Item", "the BottomView is shown")
                swipeView.isClickable = false
            }

            override fun onStartClose(layout: SwipeLayout) {
                //Log.i("Swipe List Item", "the BottomView is closed")
                swipeView.isClickable = false
            }

            override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {
                //Log.i("Swipe List Item", "hand released")
                swipeView.isClickable = true
            }
        })

        //Log.i("Swipe List Item", (swipeViewOpenStatus == SwipeLayout.Status.Open).toString() + ", " + (swipeViewOpenStatus == SwipeLayout.Status.Middle) + ", " + (swipeViewOpenStatus == SwipeLayout.Status.Close))
        if(swipeViewOpenStatus == SwipeLayout.Status.Open || swipeViewOpenStatus == SwipeLayout.Status.Middle) {
            swipeView.open()
        }

    }

    @SuppressLint("SetTextI18n")
    fun updateCurrentPriceTextView(currView: View, currentPrice: Double, entryPrice: Double) {
        val textView = currView.findViewById<TextView>(R.id.textViewPrice)
        try {
            textView.text = String.format("%.5f", currentPrice) +
                    "(${
                        String.format(
                            "%.2f",
                            (currentPrice.div(entryPrice) - 1) * 100
                        )
                    }%)"

            //Set colour of current price TextView
            if(currentPrice < entryPrice)
                textView.setTextColor(context.resources.getColor(R.color.red_text, context.resources.newTheme()))
            else if(currentPrice == entryPrice)
                textView.setTextColor(context.resources.getColor(R.color.white, context.resources.newTheme()))
            else
                textView.setTextColor(context.resources.getColor(R.color.green_text, context.resources.newTheme()))

        } catch(e: NullPointerException) {}
    }

}