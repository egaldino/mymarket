package com.bastosbf.app.msearch.service;

import android.app.IntentService;
import android.content.Intent;

import com.bastosbf.app.msearch.ProductActivity;

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
public class FindProductService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FindProductService(String name) {
        super(name);
    }
    public FindProductService(){this("FindProductService");}

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String barcode = intent.getStringExtra("barcode");
            URL url = new URL("http://pc8812.sinapad.lncc.br:8080/mymarket-server/rest/product/get?barcode="+barcode);

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
                JSONObject product = new JSONObject(response);
                String name = product.getString("name");
                String brand = product.getString("brand");
                    Intent i = new Intent(FindProductService.this, ProductActivity.class);
                    i.putExtra("name", name);
                    i.putExtra("brand", brand);

                    startActivity(i);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
