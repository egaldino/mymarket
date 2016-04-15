package com.mymarket.app.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.mymarket.app.R;
import com.mymarket.app.model.Market;
import com.mymarket.app.model.Place;
import com.mymarket.app.service.ListMarketsService;
import com.mymarket.app.service.ListPlacesService;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Spinner spinner2;
    private ProgressDialog placesProgress;
    private ProgressDialog marketsProgress;
    private ImageButton imageButton;
    private String rootURL;

    private Place place;
    private Market market;
    private ArrayList<Place> places;
    private ArrayList<Market> markets;


    private BroadcastReceiver placesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            places = (ArrayList<Place>) intent.getSerializableExtra("places");
            ArrayAdapter<Place> adapter = new ArrayAdapter<Place>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, places);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);
            SharedPreferences getPlace = getPreferences(MODE_PRIVATE);
            int position = getPlace.getInt("place", 0);
            if(position < adapter.getCount()) {
                spinner1.setSelection(position);
            }
            if(placesProgress != null && placesProgress.isShowing()) {
                placesProgress.dismiss();
            }
        }
    };
    private BroadcastReceiver marketsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
            ArrayAdapter<Market> adapter = new ArrayAdapter<Market>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, markets);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            if(marketsProgress != null && marketsProgress.isShowing()) {
                marketsProgress.dismiss();
            }
        }
    };


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("place", place);
        outState.putSerializable("market", market);
        outState.putSerializable("places", places);
        outState.putSerializable("markets", markets);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            InputStream is = getBaseContext().getAssets().open("mymarket.properties");
            Properties properties = new Properties();
            properties.load(is);
            rootURL = properties.getProperty("root.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        if(savedInstanceState == null) {
            final Intent i = new Intent(MainActivity.this, ListPlacesService.class);
            i.putExtra("root-url", rootURL);
            startService(i);
            placesProgress = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.loading), getResources().getString(R.string.places_loading_activity_main), true, true);
            placesProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    stopService(i);
                }
            });
        } else {
            place = (Place) savedInstanceState.getSerializable("place");
            market = (Market) savedInstanceState.getSerializable("market");
            places = (ArrayList<Place>) savedInstanceState.getSerializable("places");
            markets = (ArrayList<Market>) savedInstanceState.getSerializable("markets");
            {
                ArrayAdapter<Place> adapter = new ArrayAdapter<Place>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, places);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter);
                int position = adapter.getPosition(place);
                spinner1.setSelection(position);
            }
            {
                ArrayAdapter<Market> adapter = new ArrayAdapter<Market>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, markets);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter);
                int position = adapter.getPosition(market);
                spinner2.setSelection(position);
            }
        }
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                place = (Place) parent.getItemAtPosition(position);
                if (place.getId() == 0) {
                    spinner2 = (Spinner) findViewById(R.id.spinner2);
                    spinner2.setAdapter(null);
                } else {
                    SharedPreferences savePlace = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = savePlace.edit();
                    editor.putInt("place", position);
                    editor.commit();
                    final Intent i = new Intent(MainActivity.this, ListMarketsService.class);
                    i.putExtra("place", place);
                    i.putExtra("root-url", rootURL);
                    startService(i);
                    marketsProgress = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.loading), getResources().getString(R.string.markets_loading_activity_main), true, true);
                    marketsProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            stopService(i);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                market = (Market) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place place = (Place) spinner1.getSelectedItem();
                if (place == null || place.getId() == 0) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.selec_place_msn_activity_main), Toast.LENGTH_SHORT).show();
                } else {
                    scan(v);
                }
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver((placesReceiver), new IntentFilter("PLACES"));
        LocalBroadcastManager.getInstance(this).registerReceiver((marketsReceiver), new IntentFilter("MARKETS"));
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
    }

    public void suggestMarket(View view) {
        Intent i = new Intent(MainActivity.this, SuggestMarketActivity.class);
        startActivity(i);
    }

    public void fakescan(View view) {
        Intent i = new Intent();
        i.putExtra("SCAN_RESULT", "7898231840075");
        onActivityResult(49374, 0, i);
    }

    public void scan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(getResources().getString(R.string.scan_msn_activity_main));
        integrator.setCameraId(0); // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES);
    }

    public void list(View view) {
        Toast.makeText(MainActivity.this, getResources().getString(R.string.list_msn_activity_main), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IntentIntegrator.REQUEST_CODE) {
            if(data == null) {
               return;
            }
            ArrayList<Market> markets = (ArrayList) getAllItems(spinner2);
            Place place = (Place) spinner1.getSelectedItem();
            Market market = (Market) spinner2.getSelectedItem();
            Intent i = new Intent(MainActivity.this, ProductActivity.class);
            String barcode = data.getStringExtra("SCAN_RESULT");
            i.putExtra("barcode", barcode);
            i.putExtra("place", place);
            i.putExtra("market", market);
            i.putExtra("markets", markets);

            startActivity(i);
        }
    }

    public List<Object> getAllItems(Spinner spinner) {
        Adapter adapter = spinner.getAdapter();
        int n = adapter.getCount();
        List<Object> items = new ArrayList<Object>();
        for (int i = 0; i < n; i++) {
            Object item = adapter.getItem(i);
            items.add(item);
        }
        return items;
    }

    @Override
    public void onBackPressed() {

    }
}
