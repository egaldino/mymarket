package com.egaldino.app.msearch.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.egaldino.app.msearch.R;
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
public class ListPlacesService extends IntentService {


    public ListPlacesService(){
        super("FindProductService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            URL url = new URL(intent.getStringExtra("root-url") + "/rest/place/list");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = "";
            String line = null;
            while ((line = in.readLine()) != null) {
                response += line;
            }
            in.close();
            if ((connection.getResponseCode() == 200) && (!response.isEmpty())) {
                ArrayList<Place> values = new ArrayList<Place>();
                //add select message
                {
                    Place p = new Place();
                    p.setId(0);
                    p.setName(getResources().getString(R.string.select_place_service_list_places));
                    values.add(p);
                }
                JSONArray places = new JSONArray(response);
                int length = places.length();
                for (int i = 0; i < length; i++) {
                    JSONObject place = places.getJSONObject(i);
                    int id = place.getInt("id");
                    String name = place.getString("name");

                    Place p = new Place();
                    p.setId(id);
                    p.setName(name);

                    values.add(p);
                }
                Intent i = new Intent("PLACES");
                i.putExtra("places", values);
                LocalBroadcastManager.getInstance(this).sendBroadcast(i);
            } else {
                Toast.makeText(this, "Some Error Occur", Toast.LENGTH_LONG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Some Error Occur", Toast.LENGTH_LONG);
        }
    }
}
