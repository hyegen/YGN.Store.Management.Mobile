package com.ygn.ygn_store_management.Activities.ReportActivities.PurchasingReports;

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

import com.ygn.ygn_store_management.Adapters.ReportPurchasingDetailByClientDetailAdapter;
import com.ygn.ygn_store_management.Interfaces.ReportPurchasingDetailByClientDetailService;
import com.ygn.ygn_store_management.Managers.ApiUtils;
import com.ygn.ygn_store_management.Models.ReportViews.PurchasingDetailByClientDetail;
import com.ygn.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportPurchasingDetailByClientDetail extends AppCompatActivity {
    //region members
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
    //endregion

    //region overriden methods
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
    private void GetData(){
        try {
            Retrofit retrofit = ApiUtils.InitRequestWithToken(apiUrl,token);
            ReportPurchasingDetailByClientDetailService apiService = retrofit.create(ReportPurchasingDetailByClientDetailService.class);

            pleaseWait = new ProgressDialog(ReportPurchasingDetailByClientDetail.this);
            pleaseWait.setMessage("Lütfen Bekleyiniz");
            pleaseWait.setTitle("Yükleniyor...");
            pleaseWait.show();

            Call<List<PurchasingDetailByClientDetail>> call = apiService.GetPurchasingByClientDetail(token);
            call.enqueue(new Callback<List<PurchasingDetailByClientDetail>>() {
                @Override
                public void onResponse(Call<List<PurchasingDetailByClientDetail>> call, Response<List<PurchasingDetailByClientDetail>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        pleaseWait.dismiss();
                        purchasingDetailByClientDetailList = response.body();
                        reportPurchasingDetailByClientDetailAdapter.updateData(purchasingDetailByClientDetailList);
                    }
                    else{
                        Toast.makeText(ReportPurchasingDetailByClientDetail.this, response.message(), Toast.LENGTH_SHORT).show();
                        pleaseWait.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<List<PurchasingDetailByClientDetail>> call, Throwable t) {
                    Toast.makeText(ReportPurchasingDetailByClientDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    pleaseWait.dismiss();
                }
            });

            if (swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            pleaseWait.dismiss();
        }
    }
    //endregion

}