package com.example.ygn_store_management.Activities.DialogActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.Activities.SalesActivities.SalesDetailActivity;
import com.example.ygn_store_management.Adapters.ProductSelectionAdapter;
import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductSelectionDialogActivity extends AppCompatActivity {
    private List<String> dataList = new ArrayList<>();
    private Integer selectedClientId;
    private String selectedClientDescription;
    private static final String TAG = "ProductSelectionDialogActivity";
    private static String apiUrl;
    private ListView productsListView;
    private ArrayList<Product> selectedProducts = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection_dialog);
        getExtras();
        findViews();
        events();
        getSharedPreferences();
        initialize();
    }
    private void findViews(){
        productsListView=findViewById(R.id.productSelectionListView);
    }
    private void events() {

       /* Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> selectedProducts = getSelectedProducts();
                if (!selectedProducts.isEmpty()) {
                    new SaveProductsTask().execute(selectedProducts);
                } else {
                    Toast.makeText(ProductSelectionDialogActivity.this, "Lütfen bir ürün seçin", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> selectedProducts = getSelectedProducts();
                if (!selectedProducts.isEmpty()) {
                    Intent intent = new Intent(ProductSelectionDialogActivity.this, SalesDetailActivity.class);
                    intent.putExtra("selectedProducts", selectedProducts);
                    intent.putExtra("selectedClientId", selectedClientId);
                    intent.putExtra("selectedClientDescription", selectedClientDescription);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProductSelectionDialogActivity.this, "Lütfen bir ürün seçin", Toast.LENGTH_SHORT).show();
                }

                /*ArrayList<Product> selectedProducts = getSelectedProducts();
                if (!selectedProducts.isEmpty()) {
                    Intent intent = new Intent(ProductSelectionDialogActivity.this, SalesDetailActivity.class);
                    intent.putExtra("selectedProducts", selectedProducts);
                    intent.putExtra("selectedClientDescription", selectedClientDescription);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProductSelectionDialogActivity.this, "Lütfen bir ürün seçin", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }
    private void getSharedPreferences(){
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void getExtras() {
        selectedClientId = getIntent().getIntExtra("selectedClientId", -1);
        selectedClientDescription = getIntent().getStringExtra("ClientCodeAndNameAndSurname");
    }
    private void initialize(){
     new getStockAmounts().execute();
    }
    private ArrayList<Product> getSelectedProducts() {
        ArrayList<Product> selectedProducts = new ArrayList<>();
        ProductSelectionAdapter adapter = (ProductSelectionAdapter) productsListView.getAdapter();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                Product product = adapter.getItem(i);
                if (product != null && product.isSelected) {
                    selectedProducts.add(product);
                }
            }
        }
        return selectedProducts;
    }
    private class getStockAmounts extends AsyncTask<Void, Void, String> {
        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {
            String apiRoute = "/api/mobGetItemSelectionViews";
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
                        Product product = new Product();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        product.ItemCode = jsonObject.getString("ItemCode");
                        product.ItemName = jsonObject.getString("ItemName");
                        product.StockAmount = Integer.valueOf(jsonObject.getString("StockAmount"));
                        product.UnitPrice = Double.valueOf(jsonObject.getString("UnitPrice"));
                        products.add(product);
                    }

                    ProductSelectionAdapter adapter = new ProductSelectionAdapter(ProductSelectionDialogActivity.this, R.layout.adapter_products_selection, products);
                    productsListView.setAdapter(adapter);


                } catch (JSONException e) {
                    Log.e(TAG, "Hata: " + e.getMessage());
                }
            }
        }
    }


}