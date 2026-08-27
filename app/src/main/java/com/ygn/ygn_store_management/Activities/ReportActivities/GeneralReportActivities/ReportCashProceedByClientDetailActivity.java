package com.ygn.ygn_store_management.Activities.ReportActivities.GeneralReportActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ygn.ygn_store_management.Activities.DialogActivities.ClientSelectionDialogActivity;
import com.ygn.ygn_store_management.Activities.ReportActivities.SalesReports.ReportSalesDetailByClientDetail;
import com.ygn.ygn_store_management.Adapters.ClientSelectionAdapter;
import com.ygn.ygn_store_management.Adapters.ReportCashProceedClientSelectionAdapter;
import com.ygn.ygn_store_management.Adapters.ReportCashProceedListAdapter;
import com.ygn.ygn_store_management.Adapters.ReportPurchasingDetailByClientDetailAdapter;
import com.ygn.ygn_store_management.Adapters.ReportSalesDetailByClientDetailAdapter;
import com.ygn.ygn_store_management.Interfaces.ClientSelectionService;
import com.ygn.ygn_store_management.Interfaces.ReportCashProceedListViewService;
import com.ygn.ygn_store_management.Interfaces.ReportSalesDetailByClientDetailService;
import com.ygn.ygn_store_management.Managers.ApiUtils;
import com.ygn.ygn_store_management.Models.Dtos.ClientSelectionDto;
import com.ygn.ygn_store_management.Models.ReportViews.PurchasingDetailByClientDetail;
import com.ygn.ygn_store_management.Models.ReportViews.ReportCashProceedListViews;
import com.ygn.ygn_store_management.Models.ReportViews.SalesDetailByClientDetail;
import com.ygn.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportCashProceedByClientDetailActivity extends AppCompatActivity {
    private static String apiUrl;
    private String token;
    private List<ReportCashProceedListViews> reportCashProceedListViews;
    private SearchView searchView;
    protected ProgressDialog pleaseWait;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewReportCashProceedListView;
    private ReportCashProceedListAdapter reportCashProceedListViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_cash_proceed_by_client_detail);
        getSharedPreferences();
        getExtras();
        findViews();
        setMembers();
        events();
        initialize();

    }
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
                reportCashProceedListViewAdapter.filter(newText);
                return false;
            }
        });
    }
    private void initialize() {
        GetData();
    }
    private void findViews() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_cash_proceed_list_view);
        recyclerViewReportCashProceedListView = findViewById(R.id.recyclerViewReportCashProceedListView);
        searchView = findViewById(R.id.searchViewClient);
    }
    private void setMembers() {
        recyclerViewReportCashProceedListView.setLayoutManager(new LinearLayoutManager(this));
        reportCashProceedListViews = new ArrayList<>();
        reportCashProceedListViewAdapter = new ReportCashProceedListAdapter(reportCashProceedListViews);
        recyclerViewReportCashProceedListView.setAdapter(reportCashProceedListViewAdapter);
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void GetData() {
        try {
            Retrofit retrofit = ApiUtils.InitRequestWithToken(apiUrl, token);
            ReportCashProceedListViewService apiService = retrofit.create(ReportCashProceedListViewService.class);

            pleaseWait = new ProgressDialog(this);
            pleaseWait.setMessage("Lütfen Bekleyiniz");
            pleaseWait.setTitle("Yükleniyor...");
            pleaseWait.show();


            Call<List<ReportCashProceedListViews>> call = apiService.GetCashProceedListView(token);
            call.enqueue(new Callback<List<ReportCashProceedListViews>>() {
                @Override
                public void onResponse(Call<List<ReportCashProceedListViews>> call, Response<List<ReportCashProceedListViews>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        pleaseWait.dismiss();
                        reportCashProceedListViews = response.body();
                        reportCashProceedListViewAdapter.updateData(reportCashProceedListViews);
                    } else {
                        Toast.makeText(ReportCashProceedByClientDetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        pleaseWait.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<ReportCashProceedListViews>> call, Throwable t) {
                    Toast.makeText(ReportCashProceedByClientDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

}