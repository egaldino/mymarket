package com.egaldino.app.msearch.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.egaldino.app.msearch.R;
import com.egaldino.app.msearch.model.Market;
import com.egaldino.app.msearch.model.Place;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
public class ListMarketsService extends IntentService {


    public ListMarketsService(){
        super("FindProductService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Place place = (Place) intent.getSerializableExtra("place");
            URL url = new URL(intent.getStringExtra("root-url") + "/rest/market/list?place="+place.getId());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = "";
            String line = null;
            while ((line = in.readLine()) != null) {
                response += line;
            }
            in.close();
            if (!response.isEmpty()) {
                ArrayList<Market> values = new ArrayList<Market>();
                //add select message
                {
                    Market m = new Market();
                    m.setId(0);
                    m.setName(getResources().getString(R.string.select_market_service_list_market));
                    values.add(m);
                }
                JSONArray markets = new JSONArray(response);
                int length = markets.length();
                for (int i = 0; i < length; i++) {
                    JSONObject market = markets.getJSONObject(i);
                    int id = market.getInt("id");
                    String name = market.getString("name");

                    Market m = new Market();
                    m.setId(id);
                    m.setName(name);

                    values.add(m);
                }
                Intent i = new Intent("MARKETS");
                i.putExtra("markets", values);
                LocalBroadcastManager.getInstance(this).sendBroadcast(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
