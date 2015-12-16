package com.bastosbf.app.msearch.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Spinner spinner2;
    private ProgressDialog placesProgress;
    private ProgressDialog marketsProgress;
    private ImageButton imageButton;
    private String rootURL;


    private BroadcastReceiver placesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
            ArrayAdapter<Place> adapter = new ArrayAdapter<Place>(MainActivity.this, android.R.layout.simple_spinner_item, places);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);
            placesProgress.dismiss();
        }
    };
    private BroadcastReceiver marketsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
            ArrayAdapter<Market> adapter = new ArrayAdapter<Market>(MainActivity.this, android.R.layout.simple_spinner_item, markets);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            marketsProgress.dismiss();
        }
    };


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

        final Intent i = new Intent(MainActivity.this, ListPlacesService.class);
        i.putExtra("root-url", rootURL);
        startService(i);
        placesProgress = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.loading), getResources().getString(R.string.loading), true, true);
        placesProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                stopService(i);
            }
        });

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Place place = (Place) parent.getItemAtPosition(position);
                if (place.getId() == 0) {
                    spinner2 = (Spinner) findViewById(R.id.spinner2);
                    spinner2.setAdapter(null);
                } else {
                    final Intent i = new Intent(MainActivity.this, ListMarketsService.class);
                    i.putExtra("place", place);
                    i.putExtra("root-url", rootURL);
                    startService(i);
                    marketsProgress = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.loading), getResources().getString(R.string.loading), true, true);
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
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place place = (Place) spinner1.getSelectedItem();
                if(place == null || place.getId() == 0) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.selec_place_msn_activity_main), Toast.LENGTH_SHORT).show();
                } else {
                    fakescan(v);
                    //scan(v);
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
        i.putExtra("SCAN_RESULT", "000000");
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
            Intent intent = getIntent();
            Place place = (Place) spinner1.getSelectedItem();
            Market market = (Market) spinner2.getSelectedItem();
            ArrayList<Market> markets = (ArrayList) getAllItems(spinner2);
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
        List<Object> items = new ArrayList<Object>(n);
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
