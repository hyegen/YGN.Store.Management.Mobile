package com.ygn.ygn_store_management.Activities.DialogActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Activities.SalesActivities.SalesActivity;
import com.ygn.ygn_store_management.Adapters.ClientSelectionAdapter;
import com.ygn.ygn_store_management.Interfaces.ClientSelectionService;
import com.ygn.ygn_store_management.Managers.ApiUtils;
import com.ygn.ygn_store_management.Models.Dtos.ClientSelectionDto;
import com.ygn.ygn_store_management.R;
import com.ygn.ygn_store_management.Models.Client;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ClientSelectionDialogActivity extends AppCompatActivity {

    //region members
    private Integer IOCode;
    private String _currentToken;
    private ArrayList<String> clients = new ArrayList<>();
    private static String apiUrl;
    private static final String TAG = "SalesDetailActivity";
    private ListView clientsListView;
    private List<ClientSelectionDto> clientSelectionDtoList;
    private ClientSelectionAdapter clientSelectionAdapter;
    private RecyclerView recyclerViewClientSelection;
    //endregion

    //region overriden methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_selection_dialog);
        getSharedPreferences();
        findViews();
        setMembers();
        events();
        getExtras();
        initialize();

    }
    //endregion

    //region private methods
    private void getExtras() {
        IOCode = getIntent().getIntExtra("IOCode",-1);
        _currentToken = getIntent().getStringExtra("TOKEN");
    }
    private void events() {

    }
    private void setMembers(){
        recyclerViewClientSelection.setLayoutManager(new LinearLayoutManager(this));
        clientSelectionDtoList = new ArrayList<>();
        clientSelectionAdapter = new ClientSelectionAdapter(clientSelectionDtoList);
        recyclerViewClientSelection.setAdapter(clientSelectionAdapter);
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void initialize() {
        GetAllClient();
    }
    private void findViews() {
        recyclerViewClientSelection=findViewById(R.id.recyclerViewClientSelection);

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
                        clientSelectionAdapter = new ClientSelectionAdapter(response.body());
                        recyclerViewClientSelection.setAdapter(clientSelectionAdapter);
                    }
                    else{
                        Toast.makeText(ClientSelectionDialogActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        // pleaseWait.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<List<ClientSelectionDto>> call, Throwable t) {
                    Toast.makeText(ClientSelectionDialogActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
    //endregion
}