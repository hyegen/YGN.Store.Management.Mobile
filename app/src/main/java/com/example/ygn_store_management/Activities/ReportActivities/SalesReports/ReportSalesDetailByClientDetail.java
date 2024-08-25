package com.example.ygn_store_management.Activities.ReportActivities.SalesReports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ygn_store_management.Activities.ReportActivities.PurchasingReports.ReportPurchasingDetailByClientDetail;
import com.example.ygn_store_management.Adapters.ReportSalesDetailByClientDetailAdapter;
import com.example.ygn_store_management.Interfaces.ReportSalesDetailByClientDetailService;
import com.example.ygn_store_management.Managers.ApiUtils;
import com.example.ygn_store_management.Models.ReportViews.SalesDetailByClientDetail;
import com.example.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportSalesDetailByClientDetail extends AppCompatActivity {

    //region members
    protected ProgressDialog pleaseWait;
    private List<String> dataList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private static String apiUrl;
    private static final String TAG = "ReportSalesByClientDetailActivity";
    private String token;
    private RecyclerView recyclerViewSalesDetailByClientDetail;
    private List<SalesDetailByClientDetail> salesDetailByClientDetailList;
    private ReportSalesDetailByClientDetailAdapter reportSalesDetailByClientDetailAdapter;
    private SearchView searchView;
    //endregion

    //region overriden methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sales_detail_by_client_detail);
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

        if (dataList != null) {
            dataList.clear();
            dataList = null;
        }

        apiUrl = null;
        token = null;
    }
    @Override
    protected void onStop() {
        super.onStop();

        swipeRefreshLayout.setRefreshing(false);

        if (dataList != null) {
            dataList.clear();
            dataList = null;
        }
    }
    //endregion

    //region private methods
    private void getExtras() {
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
    }
    private void events() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                reportSalesDetailByClientDetailAdapter.filter(newText);
                return false;
            }
        });
    }
    private void initialize() {
        GetData();
    }
    private void findViews() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_report_sales_detail);
        recyclerViewSalesDetailByClientDetail = findViewById(R.id.recyclerViewSalesReport);
        searchView = findViewById(R.id.searchViewSales);
    }
    private void setMembers() {
        recyclerViewSalesDetailByClientDetail.setLayoutManager(new LinearLayoutManager(this));
        salesDetailByClientDetailList = new ArrayList<>();
        reportSalesDetailByClientDetailAdapter = new ReportSalesDetailByClientDetailAdapter(salesDetailByClientDetailList);
        recyclerViewSalesDetailByClientDetail.setAdapter(reportSalesDetailByClientDetailAdapter);
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void GetData() {
        try {
            Retrofit retrofit = ApiUtils.InitRequestWithToken(apiUrl, token);
            ReportSalesDetailByClientDetailService apiService = retrofit.create(ReportSalesDetailByClientDetailService.class);

            pleaseWait = new ProgressDialog(ReportSalesDetailByClientDetail.this);
            pleaseWait.setMessage("Lütfen Bekleyiniz");
            pleaseWait.setTitle("Yükleniyor...");
            pleaseWait.show();


            Call<List<SalesDetailByClientDetail>> call = apiService.GetSalesByClientDetail(token);
            call.enqueue(new Callback<List<SalesDetailByClientDetail>>() {
                @Override
                public void onResponse(Call<List<SalesDetailByClientDetail>> call, Response<List<SalesDetailByClientDetail>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        pleaseWait.dismiss();
                        salesDetailByClientDetailList = response.body();
                        reportSalesDetailByClientDetailAdapter.updateData(salesDetailByClientDetailList);
                    } else {
                        Toast.makeText(ReportSalesDetailByClientDetail.this, response.message(), Toast.LENGTH_SHORT).show();
                        pleaseWait.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<SalesDetailByClientDetail>> call, Throwable t) {
                    Toast.makeText(ReportSalesDetailByClientDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    pleaseWait.dismiss();
                }
            });

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            pleaseWait.dismiss();
        }
    }
    //endregion

}