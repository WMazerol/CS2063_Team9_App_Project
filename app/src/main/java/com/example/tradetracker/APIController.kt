package com.example.tradetracker

import android.util.JsonReader
import android.widget.TextView
import com.example.tradetracker.entity.Trade
import com.example.tradetracker.entity.TradeManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class APIController(private val activity: MainActivity) {

    private val client = OkHttpClient()

    @JvmField
    val binanceGetPriceURL = "https://api.binance.com/api/v3/ticker/price?symbol="

    private val buy = 27500 // For Testing

//    fun apiRequestURL(url: String) {
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {}
//            override fun onResponse(call: Call, response: Response) {
//                val body = response.body()?.string()
//                //println("API TEST: $body")
//                activity.runOnUiThread {
//                    //String.format((if(JSONObject(body).getString("price").toDouble() >= 1) "%.3d" else "%.5d"),
//                    activity.findViewById<TextView>(R.id.textViewPrice).text = JSONObject(body).getString("price")+
//                            "("+String.format("%.2f", (JSONObject(body).getString("price").toDouble()/buy-1)*100)+"%)"
//                }
//                var trades: ArrayList<Trade> = TradeManager().getTrades("BTCUSDT")
//                trades[0].lastPrice = JSONObject(body).getString("price").toDouble()
//            }
//        })
//    }

    fun apiRequestURLWithResponse(url: String): String? {
//        GlobalScope.launch  {
            val request = Request.Builder()
                .url(url)
                .build()

            val responseBody = client.newCall(request).execute().use {
                if (!it.isSuccessful) throw IOException("Unexpected code $it")

                val body = it.body()!!.string()
                //println("API TEST w/resp: $body")
                return@use body
            }
//
//            //return@launch responseBody
//        }
        return responseBody
    }

    fun getBinancePrice(symbol: String) : Double {
        return(JSONObject(apiRequestURLWithResponse(binanceGetPriceURL+symbol)).getDouble("price"))
    }

    fun checkSymbolIsValid(symbol: String) : Boolean {
        return try{
            return JSONObject(apiRequestURLWithResponse(binanceGetPriceURL + symbol)).getString("symbol")
                    .equals(symbol)
        } catch (e: IOException) {
            false
        }
    }

}