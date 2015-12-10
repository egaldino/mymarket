package com.bastosbf.app.msearch.service;

import android.app.IntentService;
import android.content.Intent;

import com.bastosbf.app.msearch.activity.MainActivity;
import com.bastosbf.app.msearch.model.Market;
import com.bastosbf.app.msearch.model.Place;

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
public class SuggestMarketService extends IntentService {

    public SuggestMarketService(){
        super("SuggestProductService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
            ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
            Place place = (Place) intent.getSerializableExtra("place");
            String name = intent.getStringExtra("marketName");
            String address = intent.getStringExtra("marketAddress");

            URL url = new URL(intent.getStringExtra("root-url") + "/rest/collaboration/suggest-market?name="+name+"&address="+address);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "application/json");
            InputStream is = connection.getInputStream();
            is.close();

            Intent i = new Intent(SuggestMarketService.this, MainActivity.class);
            i.putExtra("places", places);
            i.putExtra("markets", markets);
            i.putExtra("place", place);
            i.putExtra("msn", "Mercado adicionado com sucesso!");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}