package com.ygn.ygn_store_management.Activities.ReportActivities.GeneralReportActivities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ygn.ygn_store_management.Adapters.ReportOrderFilterByDateAdapter;
import com.ygn.ygn_store_management.Models.ReportViews.OrderFilterByDate;
import com.ygn.ygn_store_management.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReportOrderFilterByDateActivity extends AppCompatActivity {

    //region members
    private static String apiUrl;
    protected ProgressDialog pleaseWait;
    private static final String TAG = "ReportOrderFilterByDateActivity";
    private EditText startDateEditText;
    private EditText endDateEditText;
    private Button fetchOrderButton;
    private ListView reportSalesByClientDetilListView;
    private String token;
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
    }
    //endregion

    //region private methods
    private void getExtras() {
        Intent intent = getIntent();
        token = intent.getStringExtra("TOKEN");
    }
    private void getSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String savedIpAddress = prefs.getString("ipAddress", "");
        apiUrl = "http://" + savedIpAddress;
    }
    private void findViews() {
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        fetchOrderButton = findViewById(R.id.fetchOrderButton);
        reportSalesByClientDetilListView = findViewById(R.id.reportSalesByClientDetilListView);
    }
    private void events() {
        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));
        fetchOrderButton.setOnClickListener(v -> getOrder());
    }
    private void getOrder() {
        String startDate = (String) startDateEditText.getTag();
        String endDate = (String) endDateEditText.getTag();

        if (startDate != null && endDate != null) {
            new GetOrderByDateFiltered(startDate, endDate).execute();
        } else {
            Toast.makeText(this, "Lütfen tarih aralığını seçin", Toast.LENGTH_SHORT).show();
        }
    }
    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format(new Locale("tr", "TR"), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy", new Locale("tr", "TR"));
            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("tr", "TR"));
            try {
                String displayDate = displayFormat.format(apiFormat.parse(selectedDate));
                editText.setText(displayDate);
                editText.setTag(selectedDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    private class GetOrderByDateFiltered extends AsyncTask<Void, Void, String> {
        private String startDate;
        private String endDate;

        public GetOrderByDateFiltered(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pleaseWait = ProgressDialog.show(ReportOrderFilterByDateActivity.this, ReportOrderFilterByDateActivity.this.getResources().getString(R.string.loading), ReportOrderFilterByDateActivity.this.getResources().getString(R.string.please_wait));
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {
            String apiRoute = "/api/MobOrderDetailByDateFilter";
            try {
                URL url = new URL(apiUrl + apiRoute);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Authorization", "Bearer " + token);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("startDate", startDate);
                jsonParam.put("endDate", endDate);

                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } catch (Exception e) {
                Log.e(TAG, "Hata: " + e.getMessage());
                return null;
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String jsonData) {
            if (pleaseWait != null) {
                pleaseWait.dismiss();
            }
            if (jsonData != null) {
                try {
                    ArrayList<OrderFilterByDate> orderFilterByDates = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        OrderFilterByDate orderFilterByDate = new OrderFilterByDate();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        orderFilterByDate.OrderFicheNumber = jsonObject.getString("OrderFicheNumber");
                        orderFilterByDate.ClientName = jsonObject.getString("ClientName");
                        orderFilterByDate.ClientSurname = jsonObject.getString("ClientSurname");
                        orderFilterByDate.FirmDescription = jsonObject.getString("FirmDescription");
                        orderFilterByDate.Date_ = jsonObject.getString("Date_");
                        orderFilterByDate.TotalPrice = Double.valueOf(jsonObject.getString("TotalPrice"));

                        orderFilterByDates.add(orderFilterByDate);
                    }
                    ReportOrderFilterByDateAdapter adapter = new ReportOrderFilterByDateAdapter(ReportOrderFilterByDateActivity.this, R.layout.adapter_sales_by_client_detail, orderFilterByDates);
                    reportSalesByClientDetilListView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e(TAG, "Hata: " + e.getMessage());
                }
            }
        }
    }
    //endregion
}