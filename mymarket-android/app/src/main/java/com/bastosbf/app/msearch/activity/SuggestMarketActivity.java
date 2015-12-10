package com.bastosbf.app.msearch.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bastosbf.app.msearch.R;
import com.bastosbf.app.msearch.model.Market;
import com.bastosbf.app.msearch.model.Place;
import com.bastosbf.app.msearch.service.SuggestMarketService;
import com.bastosbf.app.msearch.service.SuggestProductService;
import com.bastosbf.app.msearch.utils.URLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class SuggestMarketActivity extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;
    private String rootURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            InputStream is = getBaseContext().getAssets().open("mymarket.properties");
            Properties properties = new Properties();
            properties.load(is);
            rootURL = properties.getProperty("root.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_suggest_market);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_market, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void suggestMarket(View view) {
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

        Intent intent = getIntent();
        ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
        ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
        Place place = (Place) intent.getSerializableExtra("place");

        String name = URLUtils.removeEmptySpaces(String.valueOf(editText1.getText()));
        String address = URLUtils.removeEmptySpaces(String.valueOf(editText2.getText()));

        if(name.isEmpty() || address.isEmpty()) {
            Toast.makeText(SuggestMarketActivity.this, "Digite o nome e o endereço do mercado!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(SuggestMarketActivity.this, SuggestMarketService.class);
        i.putExtra("marketName", name);
        i.putExtra("marketAddress", address);
        i.putExtra("places", places);
        i.putExtra("markets", markets);
        i.putExtra("place", place);
        i.putExtra("root-url", rootURL);

        startService(i);
    }
}
