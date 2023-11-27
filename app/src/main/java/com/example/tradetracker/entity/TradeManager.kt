package com.example.tradetracker.entity


class TradeManager {

    fun getTradeList(): ArrayList<Trade> {
        return tradeList
    }

    fun getTrades(symbol: String): ArrayList<Trade> {
        var trades: ArrayList<Trade> = ArrayList<Trade>()
        for(trade: Trade in tradeList) {
            if(symbol == trade.symbol) {
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
            if(tradeList[i].symbol == symbol && tradeList[i].buyPrice == buyPrice) {
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
            if(trade.stopLoss!! >= trade.lastPrice!! || trade.takeProfit!! <= trade.lastPrice!!) {
                trades.add(trade)
            }
        }
        return trades
    }

    companion object {
        private val tradeList: ArrayList<Trade> = ArrayList<Trade>()
    }

}
