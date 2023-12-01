package com.example.tradetracker

import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

private const val ALPHA_VANTAGE_API_KEY = "YG9YMFZHV0BQGERS"

class APIController() {

    private val client = OkHttpClient()
    private val stockApiClient = DefaultApi()

    private val binanceGetPriceURL = "https://api.binance.com/api/v3/ticker/price?symbol="
    private fun alphaVantageGetPriceUrl(symbol: String): String {
        return "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=$symbol&apikey=$ALPHA_VANTAGE_API_KEY"
    }

    private fun apiRequestURLWithResponse(url: String): String {
            val request = Request.Builder()
                .url(url)
                .build()

            val responseBody = client.newCall(request).execute().use {
                if (!it.isSuccessful) throw IOException("Unexpected code $it")
                return@use it.body!!.string()
            }

        return responseBody
    }

    fun getBinancePrice(symbol: String) : Double {
        return(JSONObject(apiRequestURLWithResponse(binanceGetPriceURL+symbol)).getDouble("price"))
    }

    fun getFinnhubPrice(symbol: String): Double? {
        return stockApiClient.quote(symbol).c!!.toDouble()
    }

    fun checkSymbolIsValidBinance(symbol: String): Boolean {
        return try{
            return JSONObject(apiRequestURLWithResponse(binanceGetPriceURL + symbol)).getString("symbol")
                    .equals(symbol)
        } catch (e: IOException) {
            false
        }
    }

    fun checkSymbolIsValidFinnhub(symbol: String): Boolean {
        return stockApiClient.symbolSearch(symbol).count!! > 0
    }

    init {
        ApiClient.apiKey["token"] = "cll0ktpr01qhqdq2br40cll0ktpr01qhqdq2br4g"
    }

}