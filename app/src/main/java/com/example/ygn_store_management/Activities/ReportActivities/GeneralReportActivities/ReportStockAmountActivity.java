package com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.ygn_store_management.Adapters.ReportStockAmountAdapter;
import com.example.ygn_store_management.Interfaces.StockAmountInformationService;
import com.example.ygn_store_management.Managers.ApiUtils;
import com.example.ygn_store_management.Models.ReportViews.StockAmountInformation;
import com.example.ygn_store_management.R;


import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressWarnings("deprecation")
public class ReportStockAmountActivity extends AppCompatActivity {
    protected ProgressDialog pleaseWait;
    private List<String> dataList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText edtSearchItem;
    private LinearLayout linearLayoutStockAmount;
    private static String apiUrl;
    private String token;
    private RecyclerView recyclerViewStockAmount;
    private List<StockAmountInformation> stockAmountInformationList;
    private ReportStockAmountAdapter reportStockAmountAdapter;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_stock_amount);
        getSharedPreferences();
        findViews();
        setMembers();
        events();
        getExtras();
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
                edtSearchItem.setText("");
            }
        });
        edtSearchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // searchItemByCode(s.toString());
                reportStockAmountAdapter.filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void initialize() {
/*        _getStockAmountsTask = new GetStockAmounts();
        _getStockAmountsTask.execute();*/

        GetData();
    }
    private void setMembers(){
        recyclerViewStockAmount.setLayoutManager(new LinearLayoutManager(this));
        stockAmountInformationList = new ArrayList<>();
        reportStockAmountAdapter = new ReportStockAmountAdapter(stockAmountInformationList);
        recyclerViewStockAmount.setAdapter(reportStockAmountAdapter);
    }
    private void findViews() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerViewStockAmount = findViewById(R.id.recyclerViewStockAmount);
        edtSearchItem = findViewById(R.id.edtSearchItem);
        linearLayoutStockAmount = findViewById(R.id.linearLayoutStockAmount);
    }
    private void GetData() {
        try {
            Retrofit retrofit = ApiUtils.InitRequestWithToken(apiUrl,token);
            StockAmountInformationService apiService = retrofit.create(StockAmountInformationService.class);

            Call<List<StockAmountInformation>> call = apiService.GetStockAmount(token);
            call.enqueue(new Callback<List<StockAmountInformation>>() {
                @Override
                public void onResponse(Call<List<StockAmountInformation>> call, Response<List<StockAmountInformation>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        stockAmountInformationList = response.body();
                        reportStockAmountAdapter.updateData(stockAmountInformationList);
                    }
                }
                @Override
                public void onFailure(Call<List<StockAmountInformation>> call, Throwable t) {
                    Toast.makeText(ReportStockAmountActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

         if (swipeRefreshLayout.isRefreshing()){
             swipeRefreshLayout.setRefreshing(false);
         }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(this, ReportCardViewActivity.class);
//        startActivity(intent);
        this.finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        swipeRefreshLayout.setRefreshing(false);
        edtSearchItem.addTextChangedListener(null);
        edtSearchItem.setOnEditorActionListener(null);

        linearLayoutStockAmount.removeAllViews();

        dataList.clear();
        dataList=null;
        pleaseWait = null;
        apiUrl = null;

    }
    @Override
    protected void onStop() {
        super.onStop();

        /*if (_getStockAmountsTask != null && !_getStockAmountsTask.isCancelled()) {
            _getStockAmountsTask.cancel(true);
        }
*/
        if (pleaseWait != null && pleaseWait.isShowing()) {
            pleaseWait.dismiss();
        }

        if(dataList!=null)
            dataList.clear();

    }
}