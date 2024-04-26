package com.example.ygn_store_management.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
public class MainActivity extends AppCompatActivity {
    private List<String> dataList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView productsListView;
    private static String apiUrl;
    private static final String TAG = "MainActivity";
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                new stockAmounts().execute();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
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
        new stockAmounts().execute();
    }
    private void findViews() {
        productsListView = findViewById(R.id.productsListView);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    }
    private class stockAmounts extends AsyncTask<Void, Void, String>{
        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(apiUrl);
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
                Log.e(TAG, "Error fetching data: " + e.getMessage());
            }
            return dataList.toString();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String jsonData) {
            if (jsonData != null) {
                try {
                    ArrayList<Product> students = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Product student=new Product();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        student.ItemCode = jsonObject.getString("ItemCode");
                        student.ItemName = jsonObject.getString("ItemName");
                        student.StockAmount = Integer.valueOf(jsonObject.getString("StockAmount"));
                        students.add(student);
                    }
                    ProductAdapter adapter = new ProductAdapter(MainActivity.this,R.layout.products_adapter,students);
                    productsListView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}