package com.example.ygn_store_management.Activities.ReportActivities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.Adapters.ProductAdapter;
import com.example.ygn_store_management.Adapters.ReportGeneralSalesAndPurchasingAdapter;
import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.Models.ReportViews.OrderInformationLines;
import com.example.ygn_store_management.R;
import com.google.android.material.transition.MaterialElevationScale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReportGeneralSalesAndPurchasing extends AppCompatActivity {
    private static String apiUrl;
    private static final String TAG = "ReportGeneralSalesAndPurchasingActivity";
    private EditText edtSearchOrder;
    private Button btnSearchOrder;
    private TextView txtOrderFicheNumberDescription;
    private TextView txtClientDescription;
    private TextView txtItemCodeDescription;
    private TextView txtItemNameDescription;
    private TextView txtAmountDescription;
    private TextView txtUnitPriceDescription;
    private TextView txtTotalPriceDescription;
    private TextView txtHasTaxDescription;
    private TextView txtTaxPercentageDescription;
    private TextView txtOrderNoteDescription;
    private TextView txtDateDescription;
    private ListView orderLineListView;
    private LinearLayout orderLineLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_general_sales_and_purchasing);
        getSharedPreferences();
        findViews();
        events();
        initialize();
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void findViews() {
        btnSearchOrder=findViewById(R.id.btnSearchOrder);
        edtSearchOrder=findViewById(R.id.edtSearchOrder);
        txtOrderFicheNumberDescription=findViewById(R.id.txtOrderFicheNumberDescription);
        txtClientDescription=findViewById(R.id.txtClientDescription);
        txtTotalPriceDescription=findViewById(R.id.txtTotalPriceDescription);
        txtHasTaxDescription=findViewById(R.id.txtHasTaxDescription);
        txtTaxPercentageDescription=findViewById(R.id.txtTaxPercentageDescription);
        txtOrderNoteDescription=findViewById(R.id.txtOrderNoteDescription);
        txtDateDescription=findViewById(R.id.txtDateDescription);
        orderLineListView=findViewById(R.id.orderLineListView);
        orderLineLinearLayout=findViewById(R.id.orderLineLinearLayout);
    }
    private void events() {
        btnSearchOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearchOrder.getText().toString().isEmpty())
                {
                    Toast.makeText(ReportGeneralSalesAndPurchasing.this, "Sipariş No Giriniz.", Toast.LENGTH_SHORT).show();
                    setVisibleLinearLayout();
                }
                else{
                    new GetOrderInformationByOrderFicheNumber().execute(edtSearchOrder.getText().toString());
                }
            }
        });
        btnSearchOrder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (edtSearchOrder.getText().toString().isEmpty())
                    {
                        Toast.makeText(ReportGeneralSalesAndPurchasing.this, "Sipariş No Giriniz.", Toast.LENGTH_SHORT).show();
                        setVisibleLinearLayout();
                    }
                    else
                    {
                        new GetOrderInformationByOrderFicheNumber().execute(edtSearchOrder.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });
    }
    private  void initialize(){
        setVisibleLinearLayout();
    }
    private void setVisibleLinearLayout(){
        orderLineLinearLayout.setVisibility(View.GONE);
    }
    private class GetOrderInformationByOrderFicheNumber extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String orderFicheNumber = params[0];
            String apiRoute = "/api/GetOrderDetailInformation";
            try {
                URL url = new URL(apiUrl + apiRoute + "?orderFicheNumber=" + orderFicheNumber);
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
                Toast.makeText(ReportGeneralSalesAndPurchasing.this, edtSearchOrder.getText()+" "+"Sipariş Bulunamadı. \nSipariş Numarasını kontrol ediniz", Toast.LENGTH_SHORT).show();
                edtSearchOrder.setText("");
            }
            if (jsonData != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonData);

                    String orderFicheNumber = jsonResponse.getString("OrderFicheNumber");
                    String clientName = jsonResponse.getString("ClientName");
                    String clientSurname = jsonResponse.getString("ClientSurname");
                    String firmDescription = jsonResponse.getString("FirmDescription");
                    String date_ = jsonResponse.getString("Date_");

                    txtOrderFicheNumberDescription.setText(orderFicheNumber);
                    txtClientDescription.setText(clientName + " "+clientSurname+ " - " +firmDescription);
                    txtDateDescription.setText(date_);

                   String totalPrice = String.valueOf(jsonResponse.getDouble("TotalPrice"));
                   txtTotalPriceDescription.setText(totalPrice);

                   String hasTax = jsonResponse.getString("HasTax");
                   txtHasTaxDescription.setText(hasTax);

                   String taxPercentage = String.valueOf(jsonResponse.getDouble("TaxPercentage"));
                   if (taxPercentage =="null" || taxPercentage.isEmpty())
                        txtTaxPercentageDescription.setText("0");
                   else
                       txtTaxPercentageDescription.setText(taxPercentage);

                   String orderNote = jsonResponse.getString("OrderNote");

                   if (orderNote=="null"  || orderNote.isEmpty())
                       txtOrderNoteDescription.setText("");
                    else
                        txtOrderNoteDescription.setText(orderNote);
                    JSONArray jsonArray = jsonResponse.getJSONArray("OrderLines");
                    ArrayList<OrderInformationLines> orderLines = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        OrderInformationLines orderLine = new OrderInformationLines();
                        orderLine.setItemName(item.getString("ItemName"));
                        orderLine.setAmount(item.getInt("Amount"));
                        orderLine.setUnitPrice(item.getDouble("UnitPrice"));
                        orderLine.setLineTotal(item.getInt("LineTotal"));
                        orderLines.add(orderLine);
                    }

                    ReportGeneralSalesAndPurchasingAdapter adapter = new ReportGeneralSalesAndPurchasingAdapter(ReportGeneralSalesAndPurchasing.this, R.layout.adapter_report_general_sales_and_purchasing_orderline, orderLines);
                    orderLineListView.setAdapter(adapter);
                    orderLineLinearLayout.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    Log.e(TAG, "JSON Hatası: " + e.getMessage());
                }
                edtSearchOrder.setText("");
            }
            else    {
                setVisibleLinearLayout();
            }
        }
    }
}