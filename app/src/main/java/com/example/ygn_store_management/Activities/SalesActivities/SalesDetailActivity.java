package com.example.ygn_store_management.Activities.SalesActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.Activities.MainActivities.MainCardViewActivity;
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
import java.util.Date;

public class SalesDetailActivity extends AppCompatActivity {
    private Integer IOCode;
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
                showConfirmationDialog();
            }
        });
    }
    private void getExtras() {
        Intent intent = getIntent();
        selectedProducts = (ArrayList<Product>) intent.getSerializableExtra("selectedProducts");
        selectedClientDescription = intent.getStringExtra("selectedClientDescription");
        selectedClientId = intent.getIntExtra("selectedClientId", -1);
        totalPrice=intent.getDoubleExtra("totalPrice",-1);
        IOCode=intent.getIntExtra("IOCode",-1);
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
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Satış İşlemini Tamamlamak İstiyor Musunuz?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new insertOrderToDb().execute(selectedProducts);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
                orderObject.put("CreatedDate_", formattedDate);
                orderObject.put("UpdatedDate_", formattedDate);
                orderObject.put("HasTax", 0);
                orderObject.put("PaymentType", 2);
                orderObject.put("TotalPrice", totalPrice);
                orderObject.put("IOCode", IOCode);
                orderObject.put("TransactionCode", 4);
                orderObject.put("ClientId", selectedClientId);
                orderObject.put("OrderNote", null);
                orderObject.put("OrderLines", new JSONArray());

                JSONArray orderLinesArray = orderObject.getJSONArray("OrderLines");
                for (Product product : selectedProducts) {
                    JSONObject orderLineObject = new JSONObject();

                    double lineTotal = product.Amount * product.UnitPrice;

                    orderLineObject.put("ItemId", product.getItemId());
                    orderLineObject.put("Amount", product.Amount);
                    orderLineObject.put("CreatedDate_", formattedDate);
                    orderLineObject.put("UpdatedDate_", formattedDate);
                    orderLineObject.put("LineTotal", lineTotal);
                    orderLineObject.put("IOCode", IOCode);
                    orderLineObject.put("TransactionCode", 4);

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
                finish();
            } else {
                Toast.makeText(SalesDetailActivity.this, "Ürünler kaydedilirken bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        }
    }
}