package com.example.ygn_store_management.Activities.ReportActivities.PurchasingReports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ygn_store_management.Adapters.ReportPurchasingDetailByClientDetailAdapter;
import com.example.ygn_store_management.Models.ReportViews.PurchasingDetailByClientDetail;
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

public class ReportPurchasingDetailByClientDetail extends AppCompatActivity {
    protected ProgressDialog pleaseWait;
    private List<String> dataList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText edtSearchItem;
    private ListView purchasingDetailListview;
    private static String apiUrl;
    private static final String TAG = "ReportPurchasingDetailByClientDetail";
    private GetPurchasingByClientDetail _getPurchasingByClientDetailTask;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_purchasing_detail_by_client_detail);
        getSharedPreferences();
        findViews();
        initialize();
        events();
        getExtras();
    }
    private void getExtras() {
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
    }
    private void events() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _getPurchasingByClientDetailTask = new GetPurchasingByClientDetail();
                _getPurchasingByClientDetailTask.execute();
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
        _getPurchasingByClientDetailTask = new GetPurchasingByClientDetail();
        _getPurchasingByClientDetailTask.execute();
    }
    private void findViews() {
        purchasingDetailListview = findViewById(R.id.reportPurchasingByClientDetailListView);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_report_purchasing_detail);
        edtSearchItem=findViewById(R.id.edtSearchClient);
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void searchItemByCode(String query) {
        new searchPurchasingDetail().execute(query);
    }
    private class searchPurchasingDetail extends AsyncTask<String, Void, ArrayList<PurchasingDetailByClientDetail>> {
        @Override
        protected ArrayList<PurchasingDetailByClientDetail> doInBackground(String... params) {
            String query = params[0];
            return performSearch(query);
        }

        @Override
        protected void onPostExecute(ArrayList<PurchasingDetailByClientDetail> purchases) {
            ReportPurchasingDetailByClientDetailAdapter adapter = new ReportPurchasingDetailByClientDetailAdapter(ReportPurchasingDetailByClientDetail.this, R.layout.adapter_purchasing_by_client_detail, purchases);
            purchasingDetailListview.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private ArrayList<PurchasingDetailByClientDetail> performSearch(String query) {
        ArrayList<PurchasingDetailByClientDetail> results = new ArrayList<>();

        String queryUpperCase = query.toUpperCase();

        for (String data : dataList) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                PurchasingDetailByClientDetail purchase = new PurchasingDetailByClientDetail();

                purchase.setOrderFicheNumber(jsonObject.getString("OrderFicheNumber"));
                purchase.setClientName(jsonObject.getString("ClientName"));
                purchase.setClientSurname(jsonObject.getString("ClientSurname"));
                purchase.setFirmDescription(jsonObject.getString("FirmDescription"));
                purchase.setDate_(jsonObject.getString("Date_"));
                purchase.setTotalPrice(jsonObject.getString("TotalPrice"));

                String clientNameUpperName = purchase.getClientName().toUpperCase();
                String clientSurnameUpperCase= purchase.getClientSurname().toUpperCase();
                String firmDescriptionUpperCase= purchase.getFirmDescription().toUpperCase();
                String orderFicheNumberUpperCase= purchase.getOrderFicheNumber().toUpperCase();

                if (orderFicheNumberUpperCase.contains(queryUpperCase) || clientNameUpperName.contains(queryUpperCase)|| clientSurnameUpperCase.contains(queryUpperCase)|| firmDescriptionUpperCase.contains((queryUpperCase))) {
                    results.add(purchase);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Hata: " + e.getMessage());
            }
        }
        return results;
    }
    private class GetPurchasingByClientDetail extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pleaseWait = ProgressDialog.show(ReportPurchasingDetailByClientDetail.this, ReportPurchasingDetailByClientDetail.this.getResources().getString(R.string.loading), ReportPurchasingDetailByClientDetail.this.getResources().getString(R.string.please_wait));
        }
        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {
            String apiRoute = "/api/GetPurchasingByClientDetail";
            try {
                URL url = new URL(apiUrl + apiRoute);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + token);

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
                    ArrayList<PurchasingDetailByClientDetail> purchases = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PurchasingDetailByClientDetail purchase=new PurchasingDetailByClientDetail();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        purchase.OrderFicheNumber = jsonObject.getString("OrderFicheNumber");
                        purchase.ClientName = jsonObject.getString("ClientName");
                        purchase.ClientSurname = jsonObject.getString("ClientSurname");
                        purchase.FirmDescription= jsonObject.getString("FirmDescription");
                        purchase.Date_= jsonObject.getString("Date_");
                        purchase.TotalPrice = Double.valueOf(jsonObject.getString("TotalPrice"));

                        purchases.add(purchase);
                    }
                    ReportPurchasingDetailByClientDetailAdapter adapter = new ReportPurchasingDetailByClientDetailAdapter(ReportPurchasingDetailByClientDetail.this,R.layout.adapter_purchasing_by_client_detail,purchases);
                    purchasingDetailListview.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e(TAG, "Hata: " + e.getMessage());
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (_getPurchasingByClientDetailTask != null && !_getPurchasingByClientDetailTask.isCancelled()) {
            _getPurchasingByClientDetailTask.cancel(true);
        }

        purchasingDetailListview.setAdapter(null);
        swipeRefreshLayout.setRefreshing(false);
        edtSearchItem.addTextChangedListener(null);

        if (dataList!=null)
            dataList.clear();

        if (apiUrl!=null)
            apiUrl=null;

        if (token!=null)
            token=null;
    }
}