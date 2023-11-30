# TradeTracker
 
## Release Notes:
### Release Version 1.0:
#### Existing Features:
  * **List of Trades**  
        Have a list of live trades on your main screen. Each trade has a ticker symbol, current price, entry price, take profit, stop loss, and amount invested for a quick overview of the trade. The price of
        each trade updates in real-time at a rate of roughly once per second. You can add to this list with the + button in the bottom-right corner of the screen that lets you add new trades
        without requiring any login/account. For added ease to quickly check progress on your trade, the color of the price's text and percentage profit/loss will change based on whether you have made profit (Green) or
        are at a loss (Red).
    
 * **Modify Trades**  
       By tapping on any trade in your list, you are able to bring up a screen that alows you to quickly modify that trade. You can change the entry price, take profit, stop loss, or amount values
       of your trade. By updating the values and pressing the save button, you are able to update your trade. From this screen, you also have the options to back out of the changes or delete
       the currently selected trade. These options are also included while adding new trades. Special to this screen is also the option to close the selected trade, which will remove this trade from
       your live trades list, but still keep the data of the trade including the close date of the trade.
   
 * **Trade History**  
       By accessing the menu in the top-right corner of the screen, you are able to switch between your live trade list and a list of your trade history. The trade history includes a list of
       all trades that you have closed with the most recent trades appearing first in the list and oldest trades showing up at the bottom. All information of the trade display the information that was captured when the trade was
       closed, alllowing you to review all your trades history.
   
 * **Notification System**  
       While updating the current prices of the trade; the system will send out notifications to your device to update you if the current price of the coin falls below the designated stop loss or rises to greater than
       the designated take profit. These notifications will be send out roughly once every minute untill the trade is either closed or the stop loss/take profit is updated. 

   
#### Work in Progress:
  * **UI Polishing**  
        Adding more elements and refining the UI to make the overall user experience better. Including a proper logo for the app and graphics.
  * **Swipe to Close Trade**  
        Fully implementing this feature was not able to be completed due to time restrictions. In its place currently, you are able to long press on a trade to bring up a small popup that
        allows you to quickly close a trade without entering the screen for trade modification. This was done in an effort to still keep the base functionality by just changing the gesture and
        process of accomplishing the same action.
    
  * **OnBoot Notification System**  
        Currently, the Notification System is functional only while the app is running on the device. We are currently in the process of adjusting the notification system to work
        off the device's boot completed intent, dispatched by the OS. This will allow the notification system and price updating to be continuously run in the background, battery life
        permitting.

    
#### Future Updates:
  * **Adding a New Tab for stocks:**  
        At the moment, the app is geared towards cryptocurrency but a new tab will be in the works to allow you to track stock based trades as well. This will have all the same functionality as the crypto trades.

  * **Option to Autofill Crypto/Stock ticker:**   
        This feature will give you options to autofill the trade ticker as you start typing the ticker name. For example, writing 'S' in the ticker text field under add trade should give you the option to autofill the ticker as 'SOLUSDT', 'SANDUSDT', 'SHIBUSDT' 

  * **Currency Trading Pair:**   
       At the moment, the trading assests can only be traded against US Dollar. In a future iteration for this app, we aim to implement the functionality to track tickers/stocks against other currency pairs. For example BTCEUR (against Euros) or BTCYEN (against Japanese Yen) etc.

### Supported API levels:
TradeTracker supports API levels of 26 and above.
