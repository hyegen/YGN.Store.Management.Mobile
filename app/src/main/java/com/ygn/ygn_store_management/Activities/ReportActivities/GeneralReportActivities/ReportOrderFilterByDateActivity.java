package com.ygn.ygn_store_management.Activities.ReportActivities.GeneralReportActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Adapters.ReportOrderDateFilterReportAdapter;
import com.ygn.ygn_store_management.Interfaces.ReportDateFilterSalesService;
import com.ygn.ygn_store_management.Managers.ApiUtils;
import com.ygn.ygn_store_management.Models.ReportViews.OrderFilterByDate;
import com.ygn.ygn_store_management.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportOrderFilterByDateActivity extends AppCompatActivity {

    //region members
    EditText edtStartDate, edtEndDate;
    Button btnGetData;
    RecyclerView recyclerView;
    ReportOrderDateFilterReportAdapter adapter;
    List<OrderFilterByDate> orderList = new ArrayList<>();
    private String token;
    private static String apiUrl;
    //endregion

    //region overriden methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_order_filter_by_date);
        getSharedPreferences();
        findViews();
        events();
        getExtras();

        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        btnGetData = findViewById(R.id.btnGetData);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReportOrderDateFilterReportAdapter(orderList);
        recyclerView.setAdapter(adapter);

        edtStartDate.setOnClickListener(v -> showDatePicker(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePicker(edtEndDate));

        btnGetData.setOnClickListener(v -> loadData());
    }
    //endregion
    private void loadData() {

        Retrofit retrofit = ApiUtils.InitRequestWithToken(apiUrl,token);
        ReportDateFilterSalesService apiService = retrofit.create(ReportDateFilterSalesService.class);


        apiService.getOrdersByDate(
                edtStartDate.getText().toString(),
                edtEndDate.getText().toString()
        ).enqueue(new Callback<List<OrderFilterByDate>>() {
            @Override
            public void onResponse(Call<List<OrderFilterByDate>> call,
                                   Response<List<OrderFilterByDate>> response) {

                if (response.isSuccessful()) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<OrderFilterByDate>> call, Throwable t) {
                Toast.makeText(ReportOrderFilterByDateActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showDatePicker(EditText editText) {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ReportOrderFilterByDateActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    String date =
                            selectedYear + "-" +
                                    String.format("%02d", selectedMonth + 1) + "-" +
                                    String.format("%02d", selectedDay);

                    editText.setText(date);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    //region private methods
    private void getExtras() {
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "https://" + savedIpAddress;
    }
    private void findViews() {

    }
    private void events() {

    }


    //endregion
}