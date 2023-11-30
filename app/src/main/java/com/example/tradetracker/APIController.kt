package com.example.tradetracker

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class APIController(private val activity: MainActivity) {

    private val client = OkHttpClient()

    @JvmField
    val binanceGetPriceURL = "https://api.binance.com/api/v3/ticker/price?symbol="

    fun apiRequestURLWithResponse(url: String): String {
            val request = Request.Builder()
                .url(url)
                .build()

            val responseBody = client.newCall(request).execute().use {
                if (!it.isSuccessful) throw IOException("Unexpected code $it")

                val body = it.body()!!.string()
                //println("API TEST w/resp: $body")
                return@use body
            }

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