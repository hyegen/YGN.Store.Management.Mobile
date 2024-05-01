package com.example.ygn_store_management.Activities.ReportActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ygn_store_management.Activities.MainActivities.MainCardViewActivity;
import com.example.ygn_store_management.Adapters.ProductAdapter;
import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ReportStockAmountActivity extends AppCompatActivity {
    private List<String> dataList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText edtSearchItem;
    private ListView productsListView;
    private static String apiUrl;
    private static final String TAG = "ReportStockAmountActivity";
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_stock_amount);
        getSharedPreferences();
        setPolicy();
        findViews();
        initialize();
        events();
    }
    private void events() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getStockAmounts().execute();
            }
        });
        edtSearchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItemByCode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void searchItemByCode(String query) {
        new searchProduct().execute(query);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainCardViewActivity.class);
        startActivity(intent);
        finish();
    }
    private void setPolicy(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    private void getSharedPreferences(){
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void initialize(){
        new getStockAmounts().execute();
    }
    private void findViews() {
        productsListView = findViewById(R.id.productsListView);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        edtSearchItem=findViewById(R.id.edtSearchItem);
    }
    private class getStockAmounts extends AsyncTask<Void, Void, String>{
       @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {
            String apiRoute = "/api/GetStockAmount";
            try {
                URL url = new URL(apiUrl + apiRoute);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                dataList.clear();
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    String data = jsonArray.getString(i);
                    dataList.add(data);
                }
            } catch (Exception e) {
                Log.e(TAG, "Hata: " + e.getMessage());
            }
            return dataList.toString();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String jsonData) {
            if (jsonData != null) {
                try {
                    ArrayList<Product> products = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Product product=new Product();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        product.ItemCode = jsonObject.getString("ItemCode");
                        product.ItemName = jsonObject.getString("ItemName");
                        product.StockAmount = Integer.valueOf(jsonObject.getString("StockAmount"));
                        products.add(product);
                    }
                    ProductAdapter adapter = new ProductAdapter(ReportStockAmountActivity.this,R.layout.adapter_products,products);
                    productsListView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e(TAG, "Hata: " + e.getMessage());
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private class searchProduct extends AsyncTask<String, Void, ArrayList<Product>> {
        @Override
        protected ArrayList<Product> doInBackground(String... params) {
            String query = params[0];
            return performSearch(query);
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            ProductAdapter adapter = new ProductAdapter(ReportStockAmountActivity.this, R.layout.adapter_products, products);
            productsListView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private ArrayList<Product> performSearch(String query) {
        ArrayList<Product> results = new ArrayList<>();

        String queryUpperCase = query.toUpperCase();

        for (String data : dataList) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                Product product = new Product();
                product.setItemCode(jsonObject.getString("ItemCode"));
                product.setItemName(jsonObject.getString("ItemName"));
                product.setStockAmount(jsonObject.getString("StockAmount"));

                String itemNameUpperCase = product.getItemName().toUpperCase();
                String itemCodeUpperCase = product.getItemCode().toUpperCase();

                if (itemNameUpperCase.contains(queryUpperCase) || itemCodeUpperCase.contains(queryUpperCase)) {
                    results.add(product);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Hata: " + e.getMessage());
            }
        }
        return results;
    }

}