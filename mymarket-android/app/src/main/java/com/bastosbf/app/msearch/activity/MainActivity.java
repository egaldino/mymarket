package com.bastosbf.app.msearch.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.bastosbf.app.msearch.R;
import com.bastosbf.app.msearch.model.Market;
import com.bastosbf.app.msearch.model.Place;
import com.bastosbf.app.msearch.service.FindProductService;
import com.bastosbf.app.msearch.service.ListMarketsService;
import com.bastosbf.app.msearch.service.ListPlacesService;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Spinner spinner2;
    private ImageButton imageButton;
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
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        if(intent.hasExtra("places")) {
            {
                final ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
                Place place = (Place) intent.getSerializableExtra("place");
                spinner1 = (Spinner) findViewById(R.id.spinner1);
                ArrayAdapter<Place> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, places);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter);
                final int previewsPosition = adapter.getPosition(place);
                spinner1.setSelection(previewsPosition);
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Place place = (Place) parent.getItemAtPosition(position);
                        if (place.getId() == 0) {
                            spinner2 = (Spinner) findViewById(R.id.spinner2);
                            spinner2.setAdapter(null);
                            spinner2.setClickable(false);
                            imageButton.setClickable(false);
                        } else if (previewsPosition != position || spinner2.getAdapter() == null) {
                            Intent i = new Intent(MainActivity.this, ListMarketsService.class);
                            i.putExtra("place", place);
                            i.putExtra("places", places);
                            i.putExtra("root-url", rootURL);
                            startService(i);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            if (intent.hasExtra("markets")) {
                ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
                if(markets != null) {
                    Market market = (Market) intent.getSerializableExtra("market");
                    spinner2 = (Spinner) findViewById(R.id.spinner2);
                    ArrayAdapter<Market> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, markets);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter);
                    int position = adapter.getPosition(market);
                    spinner2.setSelection(position);
                    spinner2.setClickable(true);
                    imageButton.setClickable(true);
                }
            }
        } else{
            Intent i = new Intent(MainActivity.this, ListPlacesService.class);
            i.putExtra("root-url", rootURL);
            startService(i);
        }
    }

    public void suggestMarket(View view) {
        Intent intent = getIntent();
        ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
        ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        Place place = (Place) spinner1.getSelectedItem();

        Intent i = new Intent(MainActivity.this, SuggestMarketActivity.class);
        i.putExtra("places", places);
        i.putExtra("markets", markets);
        i.putExtra("place", place);
        i.putExtra("root-url", rootURL);

        startActivity(i);

    }

    public void fakescan(View view) {
        Intent intent = getIntent();
        ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
        ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        Place place = (Place) spinner1.getSelectedItem();
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        Market market = (Market) spinner2.getSelectedItem();
        Intent i = new Intent(MainActivity.this, FindProductService.class);
        String barcode = "000000";
        i.putExtra("barcode", barcode);
        i.putExtra("places", places);
        i.putExtra("markets", markets);
        i.putExtra("place", place);
        i.putExtra("market", market);
        i.putExtra("root-url", rootURL);

        startService(i);
    }

    public void scan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(getResources().getString(R.string.scan_msn));
        integrator.setCameraId(0); // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IntentIntegrator.REQUEST_CODE) {
            Intent intent = getIntent();
            ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
            ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");

            spinner1 = (Spinner) findViewById(R.id.spinner1);
            Place place = (Place) spinner1.getSelectedItem();
            spinner2 = (Spinner) findViewById(R.id.spinner2);
            Market market = (Market) spinner2.getSelectedItem();
            Intent i = new Intent(MainActivity.this, FindProductService.class);
            String barcode = data.getStringExtra("SCAN_RESULT");
            i.putExtra("barcode", barcode);
            i.putExtra("places", places);
            i.putExtra("markets", markets);
            i.putExtra("place", place);
            i.putExtra("market", market);
            i.putExtra("root-url", rootURL);
            startService(i);
        }
    }


}
