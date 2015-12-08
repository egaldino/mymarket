package com.bastosbf.app.msearch.service;

import android.app.IntentService;
import android.content.Intent;

import com.bastosbf.app.msearch.MainActivity;
import com.bastosbf.app.msearch.ProductActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
public class ListPlacesService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ListPlacesService(String name) {
        super(name);
    }
    public ListPlacesService(){this("FindProductService");}

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            URL url = new URL("http://pc8812.sinapad.lncc.br:8080/mymarket-server/rest/place/list");

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
                JSONArray places = new JSONArray(response);
                int length = places.length();
                for (int i = 0; i < length; i++) {
                    JSONObject place = places.getJSONObject(i);
                    int id = place.getInt("id");
                    String name = place.getString("name");
                    System.out.println("ID: " + id);
                    System.out.println("Name: " + name);
                }
                Intent i = new Intent(ListPlacesService.this, MainActivity.class);
                i.putExtra("places", values);

                startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
