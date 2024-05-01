package com.example.ygn_store_management.Activities.SalesActivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ygn_store_management.Activities.DialogActivities.ProductSelectionDialogActivity;
import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SalesDetailActivity extends AppCompatActivity {
    private static String apiUrl;
    private static final String TAG = "SalesDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_detail);

        getExtras();
    }
    private void getExtras(){
        Intent intent = getIntent();
        ArrayList<Product> selectedProducts = (ArrayList<Product>) intent.getSerializableExtra("selectedProducts");
        String selectedClientDescription = intent.getStringExtra("selectedClientDescription");
        Integer selectedClientId = intent.getIntExtra("selectedClientId",-1);
    }

    private class SaveProductsTask extends AsyncTask<ArrayList<Product>, Void, Boolean> {
        @Override
        protected Boolean doInBackground(ArrayList<Product>... arrayLists) {
            ArrayList<Product> selectedProducts = arrayLists[0];
            try {
                JSONArray jsonArray = new JSONArray();
                for (Product product : selectedProducts) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ItemCode", product.ItemCode);
                    jsonObject.put("ItemName", product.ItemName);
                    jsonObject.put("StockAmount", product.StockAmount);
                    jsonObject.put("UnitPrice", product.UnitPrice);
                    jsonArray.put(jsonObject);
                }

                URL url = new URL(apiUrl + "/api/saveProduct");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(jsonArray.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error: " + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(SalesDetailActivity.this, "Ürünler başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SalesDetailActivity.this, "Ürünler kaydedilirken bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        }
    }

}