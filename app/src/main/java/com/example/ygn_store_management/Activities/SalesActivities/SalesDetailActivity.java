package com.example.ygn_store_management.Activities.SalesActivities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ygn_store_management.Activities.DialogActivities.ProductSelectionDialogActivity;
import com.example.ygn_store_management.Activities.MainActivities.MainCardViewActivity;
import com.example.ygn_store_management.Adapters.ProductListAdapter;
import com.example.ygn_store_management.Adapters.SalesDetailAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SalesDetailActivity extends AppCompatActivity {
    private ListView selectedProductListView;
    private Button confirmButton;
    private Button closeButton;
    private TextView clientDescriptionTextView;
    private TextView clientIdDescTextView;
    private TextView txtTotalPrice;
    private ArrayList<Product> selectedProducts;
    private String selectedClientDescription;
    private Integer selectedClientId;
    private Double totalPrice;
    private static String apiUrl;
    private static final String TAG = "SalesDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_detail);
        getSharedPreferences();
        findViews();
        getExtras();
        initialize();
        events();
    }
    private void getSharedPreferences(){
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void events(){
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SalesDetailActivity.this, MainCardViewActivity.class);
                startActivity(intent);
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new insertOrderToDb().execute(selectedProducts);

            }
        });
    }
    private void getExtras() {
        Intent intent = getIntent();
        selectedProducts = (ArrayList<Product>) intent.getSerializableExtra("selectedProducts");
        selectedClientDescription = intent.getStringExtra("selectedClientDescription");
        selectedClientId = intent.getIntExtra("selectedClientId", -1);
        totalPrice=intent.getDoubleExtra("totalPrice",-1);
    }
    private void initialize(){
        SalesDetailAdapter adapter = new SalesDetailAdapter(SalesDetailActivity.this,R.layout.adapter_sales_detail,selectedProducts);
        selectedProductListView.setAdapter(adapter);
        clientDescriptionTextView.setText(selectedClientDescription);
        clientIdDescTextView.setText(String.valueOf(selectedClientId));
        txtTotalPrice.setText(String.valueOf(totalPrice));
    }
    private void findViews(){
        clientIdDescTextView = findViewById(R.id.txtClientIdDesc);
        clientDescriptionTextView = findViewById(R.id.clientDescriptionTextView);
        selectedProductListView = findViewById(R.id.selectedProductListView);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        confirmButton = findViewById(R.id.confirmButton);
        closeButton = findViewById(R.id.closeButton);
    }
    private class insertOrderToDb extends AsyncTask<ArrayList<Product>, Void, Boolean> {
        @Override
        protected Boolean doInBackground(ArrayList<Product>... arrayLists) {
            ArrayList<Product> selectedProducts = arrayLists[0];
            try {
                Date currentDate = new Date();
                SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                String formattedDate = isoDateFormat.format(currentDate);


                JSONObject orderObject = new JSONObject();
                orderObject.put("DateTime", formattedDate);
                orderObject.put("TotalPrice", totalPrice);
                orderObject.put("IOCode", 2);
                orderObject.put("ClientId", selectedClientId);
                orderObject.put("OrderLines", new JSONArray());

                JSONArray orderLinesArray = orderObject.getJSONArray("OrderLines");
                for (Product product : selectedProducts) {
                    JSONObject orderLineObject = new JSONObject();
                    double lineTotal = product.Amount * product.UnitPrice;

                    orderLineObject.put("ItemId", product.getItemId());
                    orderLineObject.put("Amount", product.Amount);
                    orderLineObject.put("DateTime", formattedDate);
                    orderLineObject.put("LineTotal", lineTotal);
                    orderLineObject.put("IOCode", 2);

                    orderLinesArray.put(orderLineObject);
                }

                URL url = new URL(apiUrl + "/api/saveOrder");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(orderObject.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;

            } catch (IOException | JSONException e) {
                Log.e(TAG, "Hata: " + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(SalesDetailActivity.this, "Satış İşlemi Başarılı.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SalesDetailActivity.this, MainCardViewActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SalesDetailActivity.this, "Ürünler kaydedilirken bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        }
    }
}