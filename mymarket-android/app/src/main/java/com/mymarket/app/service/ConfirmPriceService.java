package com.mymarket.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.mymarket.app.model.Market;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ConfirmPriceService extends IntentService {


    public ConfirmPriceService(){
        super("ConfirmPriceService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String barcode = intent.getStringExtra("barcode");
            Market market = (Market) intent.getSerializableExtra("market");

            URL url = new URL(intent.getStringExtra("root-url") + "/rest/collaboration/confirm-price?market=" + market.getId() +"&barcode="+ barcode);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = "";
            String line = null;
            while ((line = in.readLine()) != null) {
                response += line;
            }
            in.close();
            int code = connection.getResponseCode();
            if (code == 200 || code == 204) {
                Intent i = new Intent("CONFIRM_PRICE");
                LocalBroadcastManager.getInstance(this).sendBroadcast(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
