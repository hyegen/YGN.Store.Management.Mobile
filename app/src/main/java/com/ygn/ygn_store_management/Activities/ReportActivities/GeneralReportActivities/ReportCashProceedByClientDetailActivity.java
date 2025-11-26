package com.ygn.ygn_store_management.Activities.ReportActivities.GeneralReportActivities;

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
import com.ygn.ygn_store_management.Adapters.ClientSelectionAdapter;
import com.ygn.ygn_store_management.Adapters.ReportCashProceedClientSelectionAdapter;
import com.ygn.ygn_store_management.Adapters.ReportPurchasingDetailByClientDetailAdapter;
import com.ygn.ygn_store_management.Interfaces.ClientSelectionService;
import com.ygn.ygn_store_management.Managers.ApiUtils;
import com.ygn.ygn_store_management.Models.Dtos.ClientSelectionDto;
import com.ygn.ygn_store_management.Models.ReportViews.PurchasingDetailByClientDetail;
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
    private RecyclerView recyclerViewClients;
    private ReportCashProceedClientSelectionAdapter clientSelectionAdapter;
    private List<ClientSelectionDto> clientSelectionDtoList;
    private SearchView searchViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_cash_proceed_by_client_detail);
        findViews();
        setMembers();
        getSharedPreferences();
        GetAllClient();
        events();

    }
    private void events() {
        searchViewClient.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                clientSelectionAdapter.filter(newText);
                return false;
            }
        });
    }
    private void setMembers() {
        recyclerViewClients.setLayoutManager(new LinearLayoutManager(this));
        clientSelectionDtoList = new ArrayList<>();
        clientSelectionAdapter = new ReportCashProceedClientSelectionAdapter(clientSelectionDtoList);
        recyclerViewClients.setAdapter(clientSelectionAdapter);
    }

    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void findViews(){
        recyclerViewClients=findViewById(R.id.recyclerViewClients);
        searchViewClient=findViewById(R.id.searchViewClient);
    }
    private void GetAllClient(){
        try {
            Retrofit retrofit = ApiUtils.InitRequestWithoutToken(apiUrl);
            ClientSelectionService apiService = retrofit.create(ClientSelectionService.class);

          /*  pleaseWait = new ProgressDialog(ReportPurchasingDetailByClientDetail.this);
            pleaseWait.setMessage("Lütfen Bekleyiniz");
            pleaseWait.setTitle("Yükleniyor...");
            pleaseWait.show();*/
            try {
                Call<List<ClientSelectionDto>> call = apiService.GetAllClient();
                call.enqueue(new Callback<List<ClientSelectionDto>>() {
                    @Override
                    public void onResponse(Call<List<ClientSelectionDto>> call, Response<List<ClientSelectionDto>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // pleaseWait.dismiss();

                            clientSelectionDtoList = response.body();
                            clientSelectionAdapter.updateData(clientSelectionDtoList);
                        }
                        else{
                            Toast.makeText(ReportCashProceedByClientDetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            // pleaseWait.dismiss();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<ClientSelectionDto>> call, Throwable t) {
                        Toast.makeText(ReportCashProceedByClientDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        //pleaseWait.dismiss();
                    }
                });
            }catch (Exception ex)
            {

            }
           /* if (swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }*/
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            //pleaseWait.dismiss();
        }
    }
}