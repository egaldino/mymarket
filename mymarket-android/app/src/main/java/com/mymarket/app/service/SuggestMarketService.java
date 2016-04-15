package com.mymarket.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SuggestMarketService extends IntentService {

    public SuggestMarketService(){
        super("SuggestProductService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String name = intent.getStringExtra("marketName");
            String address = intent.getStringExtra("marketAddress");

            URL url = new URL(intent.getStringExtra("root-url") + "/rest/collaboration/suggest-market?name="+name+"&address="+address);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "application/json");
            InputStream is = connection.getInputStream();
            is.close();

            Intent i = new Intent("SUGGEST_MARKET");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
