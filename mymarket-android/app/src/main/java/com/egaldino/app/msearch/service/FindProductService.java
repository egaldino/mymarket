package com.egaldino.app.msearch.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.egaldino.app.msearch.model.Market;
import com.egaldino.app.msearch.model.Place;
import com.egaldino.app.msearch.model.Product;
import com.egaldino.app.msearch.model.Search;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FindProductService extends IntentService {

    public FindProductService(){
        super("FindProductService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String barcode = intent.getStringExtra("barcode");
            Place place = (Place) intent.getSerializableExtra("place");
            URL url = new URL(intent.getStringExtra("root-url") + "/rest/search/prices?barcode="+barcode + "&place="+place.getId());

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
                JSONArray results = new JSONArray(response);
                ArrayList<Search> values = new ArrayList<Search>();
                int length = results.length();
                for (int i = 0; i < length; i++) {
                    JSONObject result = results.getJSONObject(i);

                    JSONObject product = result.getJSONObject("product");
                    Product p = new Product();
                    p.setBarcode(product.getString("barcode"));
                    p.setName(product.getString("name"));
                    p.setBrand(product.getString("brand"));

                    JSONObject market = result.getJSONObject("market");
                    Market m = new Market();
                    m.setId(market.getInt("id"));
                    m.setName(market.getString("name"));

                    double price = result.getDouble("price");
                    String date = result.getString("last-update");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                    Date lastUpdate = sdf.parse(date);

                    Search search = new Search();
                    search.setProduct(p);
                    search.setMarket(m);
                    search.setPrice(price);
                    search.setLastUpdate(lastUpdate);

                    values.add(search);
                }
                ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
                Market market = (Market) intent.getSerializableExtra("market");

                Intent i = new Intent("PRODUCTS");
                i.putExtra("results", values);
                i.putExtra("barcode", barcode);
                i.putExtra("markets", markets);
                i.putExtra("place", place);
                i.putExtra("market", market);

                LocalBroadcastManager.getInstance(this).sendBroadcast(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
