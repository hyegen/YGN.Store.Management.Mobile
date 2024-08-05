package com.example.ygn_store_management.Activities.ReportActivities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ygn_store_management.Adapters.ProductAdapter;
import com.example.ygn_store_management.Adapters.ReportSalesDetailByClientDetailAdapter;
import com.example.ygn_store_management.Models.Product;
import com.example.ygn_store_management.Models.ReportViews.SalesDetailByClientDetail;
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

public class ReportSalesDetailByClientDetail extends AppCompatActivity {
    protected ProgressDialog pleaseWait;
    private List<String> dataList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText edtSearchItem;
    private ListView salesDetailListview;
    private static String apiUrl;
    private static final String TAG = "ReportSalesByClientDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sales_detail_by_client_detail);
        getSharedPreferences();
        findViews();
        initialize();
        events();
    }
    private void events() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetSalesByClientDetail().execute();
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
    private void initialize() {
        new GetSalesByClientDetail().execute();
    }
    private void findViews() {
        salesDetailListview = findViewById(R.id.reportSalesByClientDetilListView);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_report_sales_detail);
        edtSearchItem=findViewById(R.id.edtSearchClient);
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void searchItemByCode(String query) {
        new searchSalesDetail().execute(query);
    }
    private class searchSalesDetail extends AsyncTask<String, Void, ArrayList<SalesDetailByClientDetail>> {
        @Override
        protected ArrayList<SalesDetailByClientDetail> doInBackground(String... params) {
            String query = params[0];
            return performSearch(query);
        }

        @Override
        protected void onPostExecute(ArrayList<SalesDetailByClientDetail> sales) {
            ReportSalesDetailByClientDetailAdapter adapter = new ReportSalesDetailByClientDetailAdapter(ReportSalesDetailByClientDetail.this, R.layout.adapter_sales_by_client_detail, sales);
            salesDetailListview.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private ArrayList<SalesDetailByClientDetail> performSearch(String query) {
        ArrayList<SalesDetailByClientDetail> results = new ArrayList<>();

        String queryUpperCase = query.toUpperCase();

        for (String data : dataList) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                SalesDetailByClientDetail sale = new SalesDetailByClientDetail();

                sale.setClientName(jsonObject.getString("ClientName"));
                sale.setClientSurname(jsonObject.getString("ClientSurname"));
                sale.setFirmDescription(jsonObject.getString("FirmDescription"));
                sale.setDate_(jsonObject.getString("Date_"));
                sale.setTotalPrice(jsonObject.getString("TotalPrice"));

                String clientNameUpperName = sale.getClientName().toUpperCase();
                String clientSurnameUpperCase= sale.getClientSurname().toUpperCase();
                String firmDescriptionUpperCase= sale.getFirmDescription().toUpperCase();

                if (clientNameUpperName.contains(queryUpperCase)|| clientSurnameUpperCase.contains(queryUpperCase)|| firmDescriptionUpperCase.contains((queryUpperCase))) {
                    results.add(sale);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Hata: " + e.getMessage());
            }
        }
        return results;
    }
    private class GetSalesByClientDetail extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pleaseWait = ProgressDialog.show(ReportSalesDetailByClientDetail.this, ReportSalesDetailByClientDetail.this.getResources().getString(R.string.loading), ReportSalesDetailByClientDetail.this.getResources().getString(R.string.please_wait));
        }
        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {
            String apiRoute = "/api/GetSalesByClientDetail";
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
            if (pleaseWait != null) {
                pleaseWait.dismiss();
            }
            if (jsonData != null) {
                try {
                    ArrayList<SalesDetailByClientDetail> sales = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        SalesDetailByClientDetail sale=new SalesDetailByClientDetail();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        sale.OrderFicheNumber = jsonObject.getString("OrderFicheNumber");
                        sale.ClientName = jsonObject.getString("ClientName");
                        sale.ClientSurname = jsonObject.getString("ClientSurname");
                        sale.FirmDescription= jsonObject.getString("FirmDescription");
                        sale.Date_= jsonObject.getString("Date_");
                        sale.TotalPrice = Double.valueOf(jsonObject.getString("TotalPrice"));

                        sales.add(sale);
                    }
                    ReportSalesDetailByClientDetailAdapter adapter = new ReportSalesDetailByClientDetailAdapter(ReportSalesDetailByClientDetail.this,R.layout.adapter_sales_by_client_detail,sales);
                    salesDetailListview.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e(TAG, "Hata: " + e.getMessage());
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}