package com.egaldino.app.msearch.activity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.egaldino.app.msearch.R;
import com.egaldino.app.msearch.model.Market;
import com.egaldino.app.msearch.model.Place;
import com.egaldino.app.msearch.service.SuggestPriceService;
import com.egaldino.app.msearch.service.SuggestProductService;
import com.egaldino.app.msearch.utils.URLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class SuggestProductActivity extends AppCompatActivity {

    private TextView textView;
    private Spinner spinner;
    private EditText editText1;
    private EditText editText2;
    private EditText editText4;
    private Button button;
    private ProgressDialog progress;
    private String rootURL;

    private static final int PRODUCT = 1;
    private static final int PRICE = 2;

    private BroadcastReceiver productSuggestionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendMessage(PRODUCT);
        }
    };

    private BroadcastReceiver priceSuggestionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendMessage(PRICE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_product);
        try {
            InputStream is = getBaseContext().getAssets().open("mymarket.properties");
            Properties properties = new Properties();
            properties.load(is);
            rootURL = properties.getProperty("root.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver((productSuggestionReceiver), new IntentFilter("SUGGEST_PRODUCT"));
        LocalBroadcastManager.getInstance(this).registerReceiver((priceSuggestionReceiver), new IntentFilter("SUGGEST_PRICE"));
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        textView = (TextView) findViewById(R.id.textView);
        spinner = (Spinner) findViewById(R.id.spinner);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button_action);

        Intent intent = getIntent();
        ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
        Place place = (Place) intent.getSerializableExtra("place");
        Market market = (Market) intent.getSerializableExtra("market");

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


        if(intent.hasExtra("productName")) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    suggestPrice(v);
                }
            });
        } else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    suggestProduct(v);
                }
            });
        }
    }

    public void suggestProduct(View view) {
        spinner = (Spinner) findViewById(R.id.spinner);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText4 = (EditText) findViewById(R.id.editText4);
        button = (Button) findViewById(R.id.button_action);
        //to avoid double clicks
        button.setEnabled(false);

        Intent intent = getIntent();
        ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
        Place place = (Place) intent.getSerializableExtra("place");
        Market market = (Market) spinner.getSelectedItem();
        if(market.getId() == 0) {
            Toast.makeText(SuggestProductActivity.this, getResources().getString(R.string.market_error_msn_activity_suggest_product), Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            return;
        }
        String barcode = String.valueOf(editText1.getText());
        String name = URLUtils.removeEmptySpaces(String.valueOf(editText2.getText()));
        String price = String.valueOf(editText4.getText());

        if(name.isEmpty() ||  price.isEmpty()) {
            Toast.makeText(SuggestProductActivity.this, getResources().getString(R.string.fields_error_msn_activity_suggest_product), Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            return;
        }

        final Intent i = new Intent(SuggestProductActivity.this, SuggestProductService.class);
        i.putExtra("markets", markets);
        i.putExtra("place", place);
        i.putExtra("market", market);
        i.putExtra("barcode", barcode);
        i.putExtra("name", name);
        i.putExtra("price", price);
        i.putExtra("root-url", rootURL);

        progress = ProgressDialog.show(SuggestProductActivity.this, getResources().getString(R.string.loading), getResources().getString(R.string.suggest_product_loading_activity_suggest_product), true, true);
        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                stopService(i);
            }
        });

        startService(i);
    }

    public void suggestPrice(View view) {
        spinner = (Spinner) findViewById(R.id.spinner);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText4 = (EditText) findViewById(R.id.editText4);
        button = (Button) findViewById(R.id.button_action);
        //to avoid double clicks
        button.setEnabled(false);

        Intent intent = getIntent();
        ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
        Place place = (Place) intent.getSerializableExtra("place");
        Market market = (Market) spinner.getSelectedItem();
        if(market.getId() == 0) {
            Toast.makeText(SuggestProductActivity.this, getResources().getString(R.string.market_error_msn_activity_suggest_product), Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            return;
        }
        String barcode = String.valueOf(editText1.getText());
        String price = String.valueOf(editText4.getText());

        if(price.isEmpty()) {
            Toast.makeText(SuggestProductActivity.this, getResources().getString(R.string.price_error_msn_activity_suggest_product), Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            return;
        }

        final Intent i = new Intent(SuggestProductActivity.this, SuggestPriceService.class);
        i.putExtra("markets", markets);
        i.putExtra("place", place);
        i.putExtra("market", market);
        i.putExtra("barcode", barcode);
        i.putExtra("price", price);
        i.putExtra("root-url", rootURL);

        progress = ProgressDialog.show(SuggestProductActivity.this, getResources().getString(R.string.loading), getResources().getString(R.string.suggest_price_loading_activity_suggest_product), true, true);
        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                stopService(i);
            }
        });

        startService(i);
    }

    private void sendMessage(int type) {
        progress.dismiss();
        if(PRODUCT == type) {
            Toast.makeText(SuggestProductActivity.this, getResources().getString(R.string.msn_service_suggest_product), Toast.LENGTH_SHORT).show();
        } else if(PRICE == type) {
            Toast.makeText(SuggestProductActivity.this, getResources().getString(R.string.msn_service_suggest_price), Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
