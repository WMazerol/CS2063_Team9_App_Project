package com.example.tradetracker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.StrictMode
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tradetracker.databinding.ActivityMainBinding
import com.example.tradetracker.entity.Trade
import com.example.tradetracker.entity.TradeAdapter
import com.example.tradetracker.layout.TradeModifier
import com.example.tradetracker.model.TradeViewModel
import kotlinx.coroutines.GlobalScope


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent
    private val alarmTimeInterval = 5 * 1000
    private val apiController = APIController(this@MainActivity)
    private lateinit var tradeViewModel: TradeViewModel
    private lateinit var listView: ListView
    private lateinit var popupMenu: View

    private val buy = 27500
    private var live = 1
    private var selectedTrade: Trade? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        tradeViewModel = ViewModelProvider(this)[TradeViewModel::class.java]

        alarmManager = (getSystemService(Context.ALARM_SERVICE) as? AlarmManager)!!
        alarmIntent = Intent(this@MainActivity, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }


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

            if (selectedTrade == null)
            {
                TradeModifier(this).createNewTrade()
            }
            else
            {
                TradeModifier(this).saveModifiedTrade(selectedTrade!!)
            }

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
        
        //GlobalScope.launch{println(">"+apiController.apiRequestURLWithResponse("https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT"))}
        findViewById<TextView>(R.id.textViewSymbol).text = "BTCUSDT"

        popupMenu = layoutInflater.inflate(R.layout.popup_window, null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true

        val popupWindow = PopupWindow(popupMenu, width, height, focusable)

        listView = findViewById(R.id.list_view)
        refreshTradeList()

        listView.setOnItemClickListener { _, _, position, _ ->
            if (live == 1)
            {
                selectedTrade = tradeViewModel.getTrades(live)[position]

                TradeModifier(this).populateEdittextsFromTrade(selectedTrade!!)

                findViewById<RelativeLayout>(R.id.layout_trade_modifier).visibility = View.VISIBLE
                binding.fab.visibility = View.INVISIBLE
                findViewById<Button>(R.id.button_trade_modifier_close).visibility = View.VISIBLE
            }
        }

        listView.setOnItemLongClickListener { _, view, position, _ ->
            if (live == 1) {
                selectedTrade = tradeViewModel.getTrades(live)[position]

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            }
            return@setOnItemLongClickListener true
        }

        popupMenu.findViewById<Button>(R.id.button_popup_cancel).setOnClickListener {
            popupWindow.dismiss()
        }

        popupMenu.findViewById<Button>(R.id.button_popup_confirm).setOnClickListener {
            TradeModifier(this).deleteTrade(selectedTrade!!)
            popupWindow.dismiss()
            refreshTradeList()
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
                GlobalScope.run {
                    for(symbol in getDistinctSymbols()) {
                        val price: Double = apiController.getBinancePrice(symbol)
                        updateLastPrice(symbol, price)
                    }
                }

                runOnUiThread {
                    refreshTradeList()
                }

                Thread.sleep(1000)
            }
        }

        run()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TradeTracker - Main", "Destroyed")
        priceThread.join()
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("TradeTracker - Main", "Restarted")
    }

    override fun onResume() {
        super.onResume()
        Log.i("TradeTracker - Main", "Resumed")
        priceThread.start()
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

    private fun getDistinctSymbols() : List<String> {
        return tradeViewModel.getDistinctSymbols()
    }

    private fun updateLastPrice(symbol: String, price: Double) {
        tradeViewModel.updateLastPrice(symbol, price)
    }

    fun getTradesPastStopOrTake(): ArrayList<Trade> {
        var trades: ArrayList<Trade> = ArrayList<Trade>()

        if(::tradeViewModel.isInitialized){
            for(trade: Trade in tradeViewModel.getTrades(live)) {
                //trade.lastPrice = APIController().apiRequestURLWithResponse(APIController().binanceGetPriceURL+trade.symbol)
                println(APIController(MainActivity()).apiRequestURLWithResponse(APIController(MainActivity()).binanceGetPriceURL+trade.symbol))
                if(trade.stopLoss!! >= trade.lastPrice!! || trade.takeProfit!! <= trade.lastPrice!!) {
                    trades.add(trade)
                }
            }
        } else
            Log.i("TradeTracker - Main", "tradeViewModel Not Initialized")

        return trades
    }
}