package com.example.tradetracker

import android.util.Log
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.infrastructure.ClientError
import io.finnhub.api.infrastructure.ClientException
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException


class APIController() {

    private val client = OkHttpClient()
    private val stockApiClient = DefaultApi()

    private val binanceGetPriceURL = "https://api.binance.com/api/v3/ticker/price?symbol="

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

    fun getBinancePrice(symbol: String) : Double? {
        return try{
            JSONObject(apiRequestURLWithResponse(binanceGetPriceURL+symbol)).getDouble("price")
        } catch (e: SocketTimeoutException) {
            //Log.i("API Controller", "Socket Timeout")
            null
        }
    }

    fun getFinnhubPrice(symbol: String): Double? {
        return try{
            stockApiClient.quote(symbol).c!!.toDouble()
        } catch (e: SocketTimeoutException) {
            //Log.i("API Controller", "Socket Timeout")
            null
        } catch (e: ClientException) {
            //Log.i("API Controller", "Client Exception: " + e.message)
            null
        }
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
        return try{
            getFinnhubPrice(symbol) != null
        } catch (e: SocketTimeoutException) {
            //Log.i("API Controller", "Socket Timeout")
            false
        } catch (e: ClientException) {
            //Log.i("API Controller", "Client Exception")
            false
        }
    }

    init {
        ApiClient.apiKey["token"] = "cll0ktpr01qhqdq2br40cll0ktpr01qhqdq2br4g"
    }

}