# TradeTracker
 
## Release Notes:
### Release Version 1.0:
#### Existing Features:
  * **List of Trades**
        Have a list of live trade tickers on your main screen. Each ticker has the symbol, price, entry, take profit, stop loss, and amount for quick viewing of the information. The price on
        each ticker updates in real-time at a rate of roughly once per second. You can add to this list with the buttom in the bottom-right corner of the screen that lets you add new tickers
        without logging into any accounts. For added ease to quickly check progress on your ticker, the color of the price's text will change based on whether you have made profit (Green) or
        are at a loss (Red).
 * **Modify Trades**
       By tapping on any trade in your list, you are able to bring up a screen that alows you to quickly modify that trade. You can change the entry, take profit, stop loss, or amount values
       of your trade. By updating the values and pressing the save button, you are able to update your ticker. From this screen, you also have the options to back out of the changes or delete
       the currently selected ticker, which are also included for adding new tickers. Special to this screen is also the option to close the selected trade, which will remove this trade from
       your live trades list, but still keep the data of the trade, and recording when the trade was closed.
 * **Trade History**
       By accessing the menu in the top-right corner of the screen, you are able to switch between your live trade list and a list of your trade history. The trade history includes a list of
       all trades that you have closed in order of most recent trade closed to least recent. All information of the trade display the information that was frozen at the time the trade was
       closed, alllowing you to review all tickers that were previously closed.
 * **Notification System**
       While updating the prices, the system will send out notifications to your device to update you if the current price of a ticker is less than the designated stop loss or greater than
       the designated take profit. These notifications will be send out roughly once every minute untill the trade is either closed or the stop loss/take profit is updated. 
   
#### Work in Progress:
  * **UI Polishing**
        Adding more elements and refining the UI to make the overall user experience better. Including proper logos for app.
  * **Swipe to Close Trade**
        Fully implementing this feature was not able to be completed due to time restrictions. In its place currently, you are able to long press on a trade to bring up a small popup that
        allows you to quickly close a trade without entering the screen to modify trades. This was done in an effort to still keep the base functionality, but just changing the gesture and
        process of accomplishing the same action.
  * **OnBoot Notification System**
        Currently, while the Notification System is functioning, it only runs while the app is running on the device. We are currently in the process of adjusting the notification to work
        off the devices boot completed intent, dispatched by the OS. This will allow the notification system and price updating to be continuously run in the background, battery life
        permitting.
    
#### Future Updates:
  * **Adding a New Tab for stocks:**
        A new tab that allows you to track tickers for stocks. This will have all the same features as the crypto tickers.

### Supported API levels:
TradeTracker supports API levels of 26 and above.
