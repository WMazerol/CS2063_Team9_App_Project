package com.example.tradetracker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tradetracker.databinding.ActivityMainBinding
import com.example.tradetracker.entity.Trade
import com.example.tradetracker.entity.TradeAdapter
import com.example.tradetracker.entity.TradeManager
import com.example.tradetracker.layout.TradeModifier
import com.example.tradetracker.model.TradeViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent
    private val alarmTimeInterval = 5 * 1000
    private val apiController = APIController(this@MainActivity)
    private lateinit var tradeViewModel: TradeViewModel
    private lateinit var listView: ListView

    private val buy = 27500
    private var live = 1
    private var selectedTrade: Trade? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarmManager = (getSystemService(Context.ALARM_SERVICE) as? AlarmManager)!!
        alarmIntent = Intent(this@MainActivity, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        tradeViewModel = ViewModelProvider(this)[TradeViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Log.i("Trade Modifier", "Open Layout")
            findViewById<RelativeLayout>(R.id.layout_trade_modifier).visibility = View.VISIBLE
            Log.i("Trade Modifier", findViewById<RelativeLayout>(R.id.layout_trade_modifier).visibility.toString())
            binding.fab.visibility = View.INVISIBLE
            findViewById<Button>(R.id.button_trade_modifier_close).visibility = View.INVISIBLE

            selectedTrade = null
        }

        findViewById<Button>(R.id.button_trade_modifier_delete).setOnClickListener {
            Log.i("Trade Modifier", "Close Layout")
            findViewById<RelativeLayout>(R.id.layout_trade_modifier).visibility = View.INVISIBLE
            binding.fab.visibility = View.VISIBLE

            if (selectedTrade == null)
            {
                TradeModifier(this).backFromTrade()
            }
            else
            {
                TradeModifier(this).deleteTrade(selectedTrade!!)
            }

            KeyboardUtils.hideKeyboard(this)
            refreshTradeList()
        }

        findViewById<Button>(R.id.button_trade_modifier_save).setOnClickListener {
            Log.i("Trade Modifier", "Save Trade")
            binding.fab.visibility = View.VISIBLE

            TradeModifier(this).saveTrade(selectedTrade!!)

            KeyboardUtils.hideKeyboard(this)
            refreshTradeList()
        }

        findViewById<Button>(R.id.button_trade_modifier_back).setOnClickListener {
            Log.i("Trade Modifier", "Back")
            findViewById<RelativeLayout>(R.id.layout_trade_modifier).visibility = View.INVISIBLE
            binding.fab.visibility = View.VISIBLE

            TradeModifier(this).backFromTrade()

            KeyboardUtils.hideKeyboard(this)
        }

        findViewById<Button>(R.id.button_trade_modifier_close).setOnClickListener {
            Log.i("Trade Modifier", "Close Trade")
            findViewById<RelativeLayout>(R.id.layout_trade_modifier).visibility = View.INVISIBLE

            binding.fab.visibility = View.VISIBLE
            TradeModifier(this).closeExistingTrade(selectedTrade!!)

            KeyboardUtils.hideKeyboard(this)
            refreshTradeList()
        }

        val notificationController = NotificationController(this, this@MainActivity)
        notificationController.checkNotificationPermissions()
        notificationController.createNotificationChannel()

        if(alarmIntent != null && alarmManager != null) {
            alarmManager.cancel(alarmIntent)
        }
        println(alarmManager == null || alarmIntent == null)
        setAlarmIntent()

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)

        //apiRequestURL("https://www.advantageonlineshopping.com/accountservice/accountrest/api/v1/health-check")

        priceThread.start()
        
        
        //GlobalScope.launch{println(">"+apiController.apiRequestURLWithResponse("https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT"))}
        findViewById<TextView>(R.id.textViewSymbol).text = "BTCUSDT"

        val BTC: Trade_Old = Trade_Old(0, "BTCUSDT", 27500.0, 30000.00, 30000.0, 1.0)
        findViewById<TextView>(R.id.textViewBuyPrice).text = "Buy Price: " + BTC.getBuyPrice().toString()
        findViewById<TextView>(R.id.textViewTakeProfit).text = "Take Profit: " + BTC.getTakeProfit().toString()
        findViewById<TextView>(R.id.textViewStopLoss).text = "Stop Loss: " + BTC.getStopLoss().toString()

        listView = findViewById(R.id.list_view)
        refreshTradeList()

        listView.setOnItemClickListener { parent, view, position, id ->
            if (live == 1)
            {
                selectedTrade = tradeViewModel.getTrades(live)[position]

                TradeModifier(this).populateEdittextsFromTrade(selectedTrade!!)

                findViewById<RelativeLayout>(R.id.layout_trade_modifier).visibility = View.VISIBLE
                binding.fab.visibility = View.INVISIBLE
                findViewById<Button>(R.id.button_trade_modifier_close).visibility = View.VISIBLE
            }
        }
    }

    private val batteryInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(intent.action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                Log.i("TradeTracker - Main", "Battery Low")
                alarmIntent.let { alarmManager.cancel(it) }
                //Toast.makeText(context, "Battery Low", Toast.LENGTH_LONG).show()
            } else if(intent.action.equals(Intent.ACTION_POWER_CONNECTED)) {
                Log.i("TradeTracker - Main", "Battery Okay")
                setAlarmIntent()
                //Toast.makeText(context, "Battery Okay", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setAlarmIntent() {
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + alarmTimeInterval,
            alarmTimeInterval.toLong(),
            alarmIntent
        )
        Log.i("TradeTracker - Main", "Alarm Intent Set")
    }

    private val priceThread = Thread {
        var running = false

        fun cancel() {
            running = false
        }

        fun run() {
            running = true
            while(running) {
                //apiController.apiRequestURL("https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT")
//                runOnUiThread() {
//                    refreshTradeList()
//                }
                Thread.sleep(1000)
            }
        }

        run()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TradeTracker - Main", "Destroyed")
        //priceThread.cancel()
        priceThread.interrupt()//.cancel()
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("TradeTracker - Main", "Restarted")
        //priceThread.run()
    }

    override fun onResume() {
        super.onResume()
        Log.i("TradeTracker - Main", "Resumed")
        //priceThread.run()
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
            R.id.tab_crypto -> {
                if (live == 1)
                {
                    live = 0
                    item.setTitle(R.string.tab_live)
                    binding.toolbar.setTitle(R.string.tab_history)
                    refreshTradeList()
                }
                else
                {
                    live = 1
                    item.setTitle(R.string.tab_history)
                    binding.toolbar.setTitle(R.string.tab_live)
                    refreshTradeList()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun refreshTradeList()
    {
        val results = tradeViewModel.getTrades(live)

        listView.adapter = TradeAdapter(this@MainActivity, results)
    }
}