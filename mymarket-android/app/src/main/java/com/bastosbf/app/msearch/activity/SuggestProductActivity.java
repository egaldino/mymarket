package com.bastosbf.app.msearch.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bastosbf.app.msearch.R;
import com.bastosbf.app.msearch.model.Market;
import com.bastosbf.app.msearch.model.Place;

import java.util.ArrayList;

public class SuggestProductActivity extends AppCompatActivity {

    private TextView textView;
    private Spinner spinner;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_product);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_suggest_product, menu);
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

        textView = (TextView) findViewById(R.id.textView);
        spinner = (Spinner) findViewById(R.id.spinner);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);

        Intent intent = getIntent();
        Place place = (Place) intent.getSerializableExtra("place");
        Market market = (Market) intent.getSerializableExtra("market");
        ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");

        textView.setText(place.getName());

        ArrayAdapter<Market> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, markets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int position = adapter.getPosition(market);
        spinner.setSelection(position);

        String barcode = intent.getStringExtra("barcode");
        editText1.setText(barcode);
        editText1.setEnabled(false);

        if(intent.hasExtra("productName")) {
            String productName = intent.getStringExtra("productName");
            editText2.setText(productName);
            editText2.setEnabled(false);

        }
        if(intent.hasExtra("productBrand")) {
            String productBrand = intent.getStringExtra("productBrand");
            editText3.setText(productBrand);
            editText3.setEnabled(false);
        }
    }

    public void suggestProduct(View view) {

    }

    public void suggestPrice(View view) {

    }
}
