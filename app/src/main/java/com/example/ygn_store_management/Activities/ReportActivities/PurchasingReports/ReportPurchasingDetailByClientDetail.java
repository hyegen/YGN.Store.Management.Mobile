package com.example.ygn_store_management.Activities.ReportActivities.PurchasingReports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ygn_store_management.Adapters.ReportPurchasingDetailByClientDetailAdapter;
import com.example.ygn_store_management.Interfaces.ReportPurchasingDetailByClientDetailService;
import com.example.ygn_store_management.Managers.ApiUtils;
import com.example.ygn_store_management.Models.ReportViews.PurchasingDetailByClientDetail;
import com.example.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportPurchasingDetailByClientDetail extends AppCompatActivity {
    protected ProgressDialog pleaseWait;
    private List<String> dataList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout relativeLayoutReportPurchasingDetailByClientDetail;
    private static String apiUrl;
    private static final String TAG = "ReportPurchasingDetailByClientDetail";
    private String token;
    private RecyclerView recyclerViewPurchasingDetailByClientDetail;
    private List<PurchasingDetailByClientDetail> purchasingDetailByClientDetailList;
    private ReportPurchasingDetailByClientDetailAdapter reportPurchasingDetailByClientDetailAdapter;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_purchasing_detail_by_client_detail);
        getSharedPreferences();
        findViews();
        setMembers();
        getExtras();
        initialize();
        events();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        swipeRefreshLayout.setRefreshing(false);

        relativeLayoutReportPurchasingDetailByClientDetail.removeAllViews();

        if(dataList!=null){
            dataList.clear();
            dataList=null;
        }

        apiUrl=null;
        token=null;

    }
    @Override
    protected void onStop() {
        super.onStop();


        if(dataList!=null){
            dataList.clear();
            dataList=null;
        }
    }
    private void getExtras() {
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
    }
    private void events() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
             /*   _getPurchasingByClientDetailTask = new GetPurchasingByClientDetail();
                _getPurchasingByClientDetailTask.execute();*/
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                reportPurchasingDetailByClientDetailAdapter.filter(newText);
                return false;
            }
        });
    }
    private void initialize() {
/*        _getPurchasingByClientDetailTask = new GetPurchasingByClientDetail();
        _getPurchasingByClientDetailTask.execute();*/
        GetData();
    }
    private void findViews() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_report_purchasing_detail);
        relativeLayoutReportPurchasingDetailByClientDetail=findViewById(R.id.relativeLayoutReportPurchasingDetailByClientDetail);

        recyclerViewPurchasingDetailByClientDetail = findViewById(R.id.recyclerViewPurchasingDetailByClientDetail);
        searchView = findViewById(R.id.searchViewPurchasing);
    }
    private void setMembers(){
        recyclerViewPurchasingDetailByClientDetail.setLayoutManager(new LinearLayoutManager(this));
        purchasingDetailByClientDetailList = new ArrayList<>();
        reportPurchasingDetailByClientDetailAdapter = new ReportPurchasingDetailByClientDetailAdapter(purchasingDetailByClientDetailList);
        recyclerViewPurchasingDetailByClientDetail.setAdapter(reportPurchasingDetailByClientDetailAdapter);
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }

   /* private void searchItemByCode(String query) {
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
    }*/

    private void GetData(){
        try {
            Retrofit retrofit = ApiUtils.InitRequestWithToken(apiUrl,token);
            ReportPurchasingDetailByClientDetailService apiService = retrofit.create(ReportPurchasingDetailByClientDetailService.class);

            Call<List<PurchasingDetailByClientDetail>> call = apiService.GetPurchasingByClientDetail(token);
            call.enqueue(new Callback<List<PurchasingDetailByClientDetail>>() {
                @Override
                public void onResponse(Call<List<PurchasingDetailByClientDetail>> call, Response<List<PurchasingDetailByClientDetail>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        purchasingDetailByClientDetailList = response.body();
                        reportPurchasingDetailByClientDetailAdapter.updateData(purchasingDetailByClientDetailList);
                    }
                    else{
                        Toast.makeText(ReportPurchasingDetailByClientDetail.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<List<PurchasingDetailByClientDetail>> call, Throwable t) {
                    Toast.makeText(ReportPurchasingDetailByClientDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            if (swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}