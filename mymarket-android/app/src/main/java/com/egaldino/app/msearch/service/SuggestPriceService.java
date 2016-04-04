package com.egaldino.app.msearch.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.egaldino.app.msearch.R;
import com.egaldino.app.msearch.model.Market;
import com.egaldino.app.msearch.model.Place;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SuggestPriceService extends IntentService {

    public SuggestPriceService(){
        super("SuggestPriceService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
            ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
            Place place = (Place) intent.getSerializableExtra("place");
            Market market = (Market) intent.getSerializableExtra("market");
            String barcode = intent.getStringExtra("barcode");
            String price = intent.getStringExtra("price");
            URL url = new URL(intent.getStringExtra("root-url") + "/rest/collaboration/suggest-price?market="+market.getId()+"&product="+barcode + "&price="+price);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "application/json");
            InputStream is = connection.getInputStream();
            is.close();

            Intent i = new Intent("SUGGEST_PRICE");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
