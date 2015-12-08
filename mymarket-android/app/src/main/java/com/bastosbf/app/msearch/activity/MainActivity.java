package com.bastosbf.app.msearch.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bastosbf.app.msearch.R;
import com.bastosbf.app.msearch.model.Market;
import com.bastosbf.app.msearch.model.Place;
import com.bastosbf.app.msearch.service.FindProductService;
import com.bastosbf.app.msearch.service.ListMarketsService;
import com.bastosbf.app.msearch.service.ListPlacesService;
import com.google.zxing.integration.android.IntentIntegrator;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner1;
    private Spinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if(intent.hasExtra("places")) {
            ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
            spinner1 = (Spinner) findViewById(R.id.spinner1);
            ArrayAdapter<Place> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, places);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Place place = (Place) parent.getItemAtPosition(position);
                    if(place.getId() != 0) {
                        Intent intent = new Intent(MainActivity.this, ListMarketsService.class);
                        intent.putExtra("place", place);
                        startService(intent);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (intent.hasExtra("markets")){
            ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
            spinner2 = (Spinner) findViewById(R.id.spinner2);
            ArrayAdapter<Market> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, markets);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
        } else{
            intent = new Intent(MainActivity.this, ListPlacesService.class);
            startService(intent);
        }
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
            Intent intent = new Intent(MainActivity.this, FindProductService.class);
            String barcode = data.getStringExtra("SCAN_RESULT");
            intent.putExtra("barcode", barcode);
            startService(intent);
        }
    }


}
