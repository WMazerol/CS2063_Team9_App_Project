package com.example.tradetracker

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class TradeManager {

    fun getTradeList(): ArrayList<Trade> {
        return tradeList
    }

    fun getTrades(symbol: String): ArrayList<Trade> {
        var trades: ArrayList<Trade> = ArrayList<Trade>()
        for(trade: Trade in tradeList) {
            if(symbol == trade.getSymbol()) {
                trades.add(trade)
            }
        }
        return trades
    }

    fun addToTradeList(trade: Trade) {
        tradeList.add(trade)
    }

    fun removeFromTradeList(symbol: String, buyPrice: Double): Boolean {
        for(i in 0..tradeList.size step 1) {
            if(tradeList[i].getSymbol() == symbol && tradeList[i].getBuyPrice() == buyPrice) {
                tradeList.removeAt(i)
                return true
            }
        }
        return false
    }

    fun getTradesPastStopOrTake(): ArrayList<Trade> {
//        var apiController = APIController(MainActivity())
//        var trades: ArrayList<Trade> = ArrayList<Trade>()
//        for(t: Trade in tradeList) {
//            val apiResponse = apiController.apiRequestURLWithResponse(apiController.binanceGetPriceURL+t.getSymbol())
//            println(apiResponse)
//            try {
//                val currentPrice = JSONObject(apiResponse).getString("price").toDouble()
//                t.setLastPrice(currentPrice)
//
//                if(t.getStopLoss() >= currentPrice || t.getTakeProfit() <= currentPrice) {
//                    trades.add(t)
//                }
//            } catch (e: JSONException) {
//                println("Empty Response")
//            }
//
//        }
        var trades: ArrayList<Trade> = ArrayList<Trade>()
        for(trade: Trade in tradeList) {
            println(trade.getLastPrice())
            if(trade.getStopLoss() >= trade.getLastPrice() || trade.getTakeProfit() <= trade.getLastPrice()) {
                trades.add(trade)
            }
        }
        return trades
    }

    companion object {
        private val tradeList: ArrayList<Trade> = ArrayList<Trade>()
    }

}
