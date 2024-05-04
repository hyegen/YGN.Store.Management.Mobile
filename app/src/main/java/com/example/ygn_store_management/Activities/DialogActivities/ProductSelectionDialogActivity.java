package com.example.ygn_store_management.Activities.DialogActivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.Activities.PurchasingActivities.PurchasingDetailActivity;
import com.example.ygn_store_management.Activities.ReportActivities.ReportStockAmountActivity;
import com.example.ygn_store_management.Activities.SalesActivities.SalesDetailActivity;
import com.example.ygn_store_management.Adapters.ProductAdapter;
import com.example.ygn_store_management.Adapters.ProductListAdapter;
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
    private Integer IOCode;
    private EditText edtSearchItem;
    private Button confirmButton;
    private List<String> dataList = new ArrayList<>();
    private Integer selectedClientId;
    private Double lineTotal;
    private Double totalPrice;
    private Integer selectedItemId;
    private String selectedClientDescription;
    private static final String TAG = "ProductSelectionDialogActivity";
    private static String apiUrl;
    private ListView productsListView;
    private ArrayList<Integer> inputAmounts = new ArrayList<>();
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
        edtSearchItem=findViewById(R.id.edtSearchItem);
        confirmButton = findViewById(R.id.confirmButton);
    }
    private void events() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> selectedProducts = getSelectedProducts();
                if(!selectedProducts.isEmpty()){
                    if(IOCode == 1){
                        Intent intent = new Intent(ProductSelectionDialogActivity.this, PurchasingDetailActivity.class);
                        intent.putExtra("selectedProducts", selectedProducts);
                        intent.putExtra("selectedClientId", selectedClientId);
                        intent.putExtra("selectedClientDescription", selectedClientDescription);
                        intent.putExtra("quantities", inputAmounts);
                        intent.putExtra("totalPrice",totalPrice);
                        intent.putExtra("IOCode",IOCode);
                        startActivity(intent);
                    } else if (IOCode == 2) {
                        Intent intent = new Intent(ProductSelectionDialogActivity.this, SalesDetailActivity.class);
                        intent.putExtra("selectedProducts", selectedProducts);
                        intent.putExtra("selectedClientId", selectedClientId);
                        intent.putExtra("selectedClientDescription", selectedClientDescription);
                        intent.putExtra("quantities", inputAmounts);
                        intent.putExtra("totalPrice",totalPrice);
                        intent.putExtra("IOCode",IOCode);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(ProductSelectionDialogActivity.this, "Bir şeyler ters gitti.", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(ProductSelectionDialogActivity.this, "Ürün Seçiniz", Toast.LENGTH_SHORT).show();
                }

            }
        });
        edtSearchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItem(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
        IOCode = getIntent().getIntExtra("IOCode",-1);
    }
    private void initialize(){
     new getProducts().execute();
    }
    private ArrayList<Product> getSelectedProducts() {
        ArrayList<Product> selectedProducts = new ArrayList<>();
        totalPrice=0.0;
        ProductSelectionAdapter adapter = (ProductSelectionAdapter) productsListView.getAdapter();
        if (adapter != null) {
            int count = adapter.getCount();
            for (int i = 0; i < count; i++) {
                Product product = adapter.getItem(i);
                if (product != null && product.isSelected) {
                    int quantity = 0;
                    // if (i < adapter.quantities.size()) {
                    quantity = adapter.quantities.get(i);
                    // }

                    product.setAmount(quantity);
                    lineTotal=quantity* product.getUnitPrice().doubleValue();
                    totalPrice+=lineTotal;
                    selectedProducts.add(product);
                }
            }


        }
        return selectedProducts;
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
                product.setUnitPrice(jsonObject.getString("UnitPrice"));

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
    private void searchItem(String query) {
        new searchProduct().execute(query);
    }
    private class getProducts extends AsyncTask<Void, Void, String> {
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
                        product.ItemId=jsonObject.getInt("ItemId");
                        product.ItemCode = jsonObject.getString("ItemCode");
                        product.ItemName = jsonObject.getString("ItemName");
                        product.StockAmount = Integer.valueOf(jsonObject.getString("StockAmount"));
                        product.UnitPrice = Double.valueOf(jsonObject.getString("UnitPrice"));
                        products.add(product);
                    }

                    ProductSelectionAdapter adapter = new ProductSelectionAdapter(ProductSelectionDialogActivity.this, R.layout.adapter_products_selection, products,inputAmounts);
                    productsListView.setAdapter(adapter);

                } catch (JSONException e) {
                    Log.e(TAG, "Hata: " + e.getMessage());
                }
            }
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
            ProductSelectionAdapter adapter = new ProductSelectionAdapter(ProductSelectionDialogActivity.this, R.layout.adapter_products_selection, products,inputAmounts);
            productsListView.setAdapter(adapter);
        }
    }
}