package com.example.tradetracker

import android.content.Context
import android.widget.TextView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class APIController(private val activity: MainActivity) {

    private val client = OkHttpClient()

    private val buy = 27500 // For Testing

    fun apiRequestURL(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println("API TEST: $body")
                activity.runOnUiThread {
                    activity.findViewById<TextView>(R.id.textViewPrice).text = JSONObject(body).getString("price")+"("+String.format("%.2f", (JSONObject(body).getString("price").toDouble()/buy-1)*100)+"%)"
                }
            }
        })
    }

}