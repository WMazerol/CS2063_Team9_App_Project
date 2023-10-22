package com.example.tradetracker

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tradetracker.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val alarmManager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    private val alarmIntent = Intent(this@MainActivity, AlarmReceiver::class.java).let { intent ->
        PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
    val alarmTimeInterval = 5 * 100

    private val client = OkHttpClient()
    val buy = 27500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Not Yet Implemented.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val notificationController = NotificationController(this, this@MainActivity)
        notificationController.checkNotificationPermissions()
        notificationController.createNotificationChannel()

        if(alarmIntent != null && alarmManager != null) {
            alarmManager.cancel(alarmIntent)
        }

        setAlarmIntent()

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)

        //apiRequestURL("https://www.advantageonlineshopping.com/accountservice/accountrest/api/v1/health-check")
        apiRequestURL("https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT")
        findViewById<TextView>(R.id.textViewSymbol).text = "BTCUSDT"
        findViewById<TextView>(R.id.textViewBuyPrice).text = buy.toString()

    }

    private val batteryInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(intent.action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                Log.i("TradeTracker - Main", "Battery Low")
                alarmManager?.cancel(alarmIntent)
                //Toast.makeText(context, "Battery Low", Toast.LENGTH_LONG).show()
            } else if(intent.action.equals(Intent.ACTION_POWER_CONNECTED)) {
                Log.i("TradeTracker - Main", "Battery Okay")
                setAlarmIntent()
                //Toast.makeText(context, "Battery Okay", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun apiRequestURL(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println("API TEST: $body")
                runOnUiThread {
                    findViewById<TextView>(R.id.textViewPrice).text = JSONObject(body).getString("price")+"("+String.format("%.2f", (JSONObject(body).getString("price").toDouble()/buy-1)*100)+"%)"
                }
            }
        })
    }

    private fun setAlarmIntent() {
        alarmManager?.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + alarmTimeInterval,
            alarmTimeInterval.toLong(),
            alarmIntent
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}