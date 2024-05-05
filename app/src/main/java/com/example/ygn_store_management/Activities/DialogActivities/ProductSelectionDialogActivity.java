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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private ImageButton btnSearch;
    private Integer selectedClientId;
    private Double lineTotal;
    private Double totalPrice;
    private String selectedClientDescription;
    private static final String TAG = "ProductSelectionDialogActivity";
    private static String apiUrl;
    private ListView productsListView;
    private ArrayList<Integer> inputAmounts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_product_selection_dialog);
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
        btnSearch = findViewById(R.id.btnSearch);
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
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getProductByItemCode().execute(edtSearchItem.getText().toString());
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
        IOCode = getIntent().getIntExtra("IOCode",-1);
    }
    private void initialize(){
        // new getProducts().execute(edtSearchItem.getText().toString());
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
    private class getProductByItemCode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String itemCode = params[0];
            String apiRoute = "/api/getProductByItemCode";
            try {
                URL url = new URL(apiUrl + apiRoute + "?itemCode=" + itemCode);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } catch (Exception e) {
                Log.e(TAG, "Hata: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonData) {
            if(jsonData.equals("null")){
                Toast.makeText(ProductSelectionDialogActivity.this, edtSearchItem.getText()+" "+"Kodlu Ürün Bulunamadı", Toast.LENGTH_SHORT).show();
                edtSearchItem.setText("");
            }
            if (jsonData != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonData);

                    int itemId = jsonResponse.getInt("ItemId");
                    String itemCode = jsonResponse.getString("ItemCode");
                    String itemName = jsonResponse.getString("ItemName");
                    int stockAmount = jsonResponse.getInt("StockAmount");
                    double unitPrice = jsonResponse.getDouble("UnitPrice");

                    Product product = new Product();
                    product.ItemId = itemId;
                    product.ItemCode = itemCode;
                    product.ItemName = itemName;
                    product.StockAmount = stockAmount;
                    product.UnitPrice = unitPrice;

                    ArrayList<Product> products = new ArrayList<>();
                    products.add(product);
                    ProductSelectionAdapter adapter = (ProductSelectionAdapter) productsListView.getAdapter();
                    if (adapter != null) {
                        adapter.updateData(products);
                    } else {
                        adapter = new ProductSelectionAdapter(ProductSelectionDialogActivity.this, R.layout.adapter_products_selection, products, inputAmounts);
                        productsListView.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "JSON Hatası: " + e.getMessage());
                }
                edtSearchItem.setText("");
            }

        }
    }
}