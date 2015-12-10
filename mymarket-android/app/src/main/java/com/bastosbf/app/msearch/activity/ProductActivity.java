package com.bastosbf.app.msearch.activity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bastosbf.app.msearch.R;
import com.bastosbf.app.msearch.model.Market;
import com.bastosbf.app.msearch.model.Place;
import com.bastosbf.app.msearch.model.Product;
import com.bastosbf.app.msearch.model.Search;
import com.bastosbf.app.msearch.service.ListMarketsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private ListView listView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product, menu);
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);

        Intent intent = getIntent();
        ArrayList<Search> results = (ArrayList<Search>) intent.getSerializableExtra("results");
        if(results.isEmpty()) {
            textView1.setText("Produto não encontrado!");
            String barcode = intent.getStringExtra("barcode");
            textView2.setText(barcode);
            textView2.setVisibility(View.INVISIBLE);
            button.setText("Adicionar produto");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    String barcode = String.valueOf(textView2.getText());
                    ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
                    ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
                    Place place = (Place) intent.getSerializableExtra("place");
                    Market market = (Market) intent.getSerializableExtra("market");

                    Intent i = new Intent(ProductActivity.this, SuggestProductActivity.class);
                    i.putExtra("barcode", barcode);
                    i.putExtra("places", places);
                    i.putExtra("markets", markets);
                    i.putExtra("place", place);
                    i.putExtra("market", market);

                    startActivity(i);
                }
            });
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            Product product = null;
            for (Search result : results) {
                if(product == null) {
                    product = result.getProduct();
                    textView1.setText(product.getName() + " - " + product.getBrand());
                    textView2.setText(product.getBarcode());
                }
                Market market = result.getMarket();
                Date lastUpdate = result.getLastUpdate();
                Map<String, Object> map = new HashMap<>();
                map.put("info", market.getName() + " - R$ " + result.getPrice());
                map.put("date", "Última atualização: " + sdf.format(lastUpdate));
                list.add(map);
            }
            BaseAdapter adapter = new SimpleAdapter(this, list,
                    android.R.layout.simple_list_item_2,
                    new String[]{"info", "date"},
                    new int[]{android.R.id.text1, android.R.id.text2});
            listView.setDivider(new ColorDrawable(this.getResources().getColor(R.color.white)));
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            listView.setDividerHeight(height / 35);
            listView.setBackgroundColor(getResources().getColor(R.color.white));
            listView.setAdapter(adapter);
            button.setText("Atualizar preço");
            final String productName = product.getName();
            final String productBrand = product.getBrand();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    String barcode = String.valueOf(textView2.getText());
                    ArrayList<Place> places = (ArrayList<Place>) intent.getSerializableExtra("places");
                    ArrayList<Market> markets = (ArrayList<Market>) intent.getSerializableExtra("markets");
                    Place place = (Place) intent.getSerializableExtra("place");
                    Market market = (Market) intent.getSerializableExtra("market");

                    Intent i = new Intent(ProductActivity.this, SuggestProductActivity.class);
                    i.putExtra("barcode", barcode);
                    i.putExtra("places", places);
                    i.putExtra("markets", markets);
                    i.putExtra("place", place);
                    i.putExtra("market", market);
                    i.putExtra("productName", productName);
                    i.putExtra("productBrand", productBrand);

                    startActivity(i);
                }
            });
        }
    }
}
