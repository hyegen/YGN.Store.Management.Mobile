package com.ygn.ygn_store_management.Activities.PurchasingActivities;

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

import com.ygn.ygn_store_management.Activities.MainActivities.MainCardViewActivity;
import com.ygn.ygn_store_management.Adapters.PurchasingDetailAdapter;
import com.ygn.ygn_store_management.Models.Product;
import com.ygn.ygn_store_management.R;

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

public class PurchasingDetailActivity extends AppCompatActivity {

    //region members
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
    private static final String TAG = "PurchasingDetailActivity";
    //endregion

    //region overriden methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasing_detail);
        getSharedPreferences();
        findViews();
        getExtras();
        initialize();
        events();
    }
    //endregion

    //region private methods
    private void getSharedPreferences(){
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void events(){
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PurchasingDetailActivity.this, MainCardViewActivity.class);
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
        PurchasingDetailAdapter adapter = new PurchasingDetailAdapter(PurchasingDetailActivity.this,R.layout.adapter_purchasing_detail,selectedProducts);
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
        builder.setTitle("Satınalma İşlemini Tamamlamak İstiyor Musunuz?");
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
                orderObject.put("DateTime", formattedDate);
                orderObject.put("TotalPrice", totalPrice);
                orderObject.put("IOCode", IOCode);
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
                    orderLineObject.put("IOCode", IOCode);

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
                Toast.makeText(PurchasingDetailActivity.this, "Satınalma İşlemi Başarılı.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PurchasingDetailActivity.this, MainCardViewActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(PurchasingDetailActivity.this, "Ürünler kaydedilirken bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //endregion
}