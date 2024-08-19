package com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ygn_store_management.Adapters.ReportOrderLineInformationAdapter;
import com.example.ygn_store_management.Interfaces.OrderLineInformationService;
import com.example.ygn_store_management.Managers.ApiUtils;
import com.example.ygn_store_management.Models.ReportViews.OrderInformation;
import com.example.ygn_store_management.Models.ReportViews.ReportOrderInformationLines;
import com.example.ygn_store_management.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportGeneralSalesAndPurchasing extends AppCompatActivity {
    private static String apiUrl;
    private EditText edtSearchOrder;
    private Button btnSearchOrder;
    private Button btnShowOrderNotePopUp;
    private TextView txtOrderFicheNumberDescription;
    private TextView txtClientDescription;
    private TextView txtTotalPriceDescription;
    private TextView txtHasTaxDescription;
    private TextView txtTaxPercentageDescription;
    private TextView txtDateDescription;
    private LinearLayout orderLineLinearLayout;
    private RelativeLayout relativeLayoutReportGeneralSalesAndPurchasing;
    protected ProgressDialog pleaseWait;
    public String _currentOrderNote;
    private String token;
    private ReportOrderLineInformationAdapter orderLineInformationAdapter;
    private RecyclerView recyclerViewOrderLine;
    private List<ReportOrderInformationLines> orderInformationLinesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_general_sales_and_purchasing);
        getSharedPreferences();
        findViews();
        events();
        initialize();
        setMembers();
        getExtras();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        btnSearchOrder.setOnClickListener(null);
        edtSearchOrder.setOnEditorActionListener(null);
        txtOrderFicheNumberDescription.setText(null);
        txtClientDescription.setText(null);
        txtTotalPriceDescription.setText(null);
        txtHasTaxDescription.setText(null);
        txtTaxPercentageDescription.setText(null);
        txtDateDescription.setText(null);

        relativeLayoutReportGeneralSalesAndPurchasing.removeAllViews();

        orderLineLinearLayout.removeAllViews();
        btnShowOrderNotePopUp.setOnClickListener(null);

        apiUrl=null;
        _currentOrderNote=null;
        token = null;

        this.finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    protected void onStop() {
        super.onStop();



        if (pleaseWait != null && pleaseWait.isShowing()) {
            pleaseWait.dismiss();
        }

    }
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
        btnSearchOrder=findViewById(R.id.btnSearchOrder);
        edtSearchOrder=findViewById(R.id.edtSearchOrder);
        txtOrderFicheNumberDescription=findViewById(R.id.txtOrderFicheNumberDescription);
        txtClientDescription=findViewById(R.id.txtClientDescription);
        txtTotalPriceDescription=findViewById(R.id.txtTotalPriceDescription);
        txtHasTaxDescription=findViewById(R.id.txtHasTaxDescription);
        txtTaxPercentageDescription=findViewById(R.id.txtTaxPercentageDescription);
        txtDateDescription=findViewById(R.id.txtDateDescription);
        orderLineLinearLayout=findViewById(R.id.orderLineLinearLayout);
        btnShowOrderNotePopUp=findViewById(R.id.btnShowOrderNotePopUp);
        relativeLayoutReportGeneralSalesAndPurchasing=findViewById(R.id.relativeLayoutReportGeneralSalesAndPurchasing);
        recyclerViewOrderLine = findViewById(R.id.recyclerViewOrderLine);
    }
    private void events() {
        btnSearchOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (validateGetData())
                   GetData();
            }
        });
        btnShowOrderNotePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog( _currentOrderNote);
            }
        });

        //        edtSearchOrder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                // 'event' null mı değil mi kontrol ediliyor
//                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    _getOrderInformationByOrderFicheNumberTask = new GetOrderInformationByOrderFicheNumber();
//                    _getOrderInformationByOrderFicheNumberTask.execute(edtSearchOrder.getText().toString());
//                    return true;
//                }
//                return false;
//            }
//        });
    }
    private void showDialog(String message) {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.pop_up_order_note_layout);

        TextView txtPopupMessage = dialog.findViewById(R.id.txtPopupMessage);
        txtPopupMessage.setText(message);

        dialog.setCancelable(true);

        Button closeButton = dialog.findViewById(R.id.btnClosePopup);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();
    }
    private  void initialize(){
        setVisibleLinearLayout();
    }
    private void setVisibleLinearLayout(){
        orderLineLinearLayout.setVisibility(View.GONE);
    }
    private void setMembers(){
        recyclerViewOrderLine.setLayoutManager(new LinearLayoutManager(this));
        orderInformationLinesList = new ArrayList<>();
        orderLineInformationAdapter = new ReportOrderLineInformationAdapter(orderInformationLinesList);
        recyclerViewOrderLine.setAdapter(orderLineInformationAdapter);
    }
    private void GetData(){
        try {
            Retrofit retrofit = ApiUtils.InitRequestWithToken(apiUrl,token);
            OrderLineInformationService apiService = retrofit.create(OrderLineInformationService.class);
            Call<OrderInformation> call = apiService.GetOrderDetailInformation(token,edtSearchOrder.getText().toString());
            call.enqueue(new Callback<OrderInformation>() {
                @Override
                public void onResponse(Call<OrderInformation> call, Response<OrderInformation> response) {
                    if (response.isSuccessful() && response.body() != null) {

                            OrderInformation orderInfo = response.body();
                            if (orderInfo != null) {

                                txtOrderFicheNumberDescription.setText(orderInfo.getOrderFicheNumber());

                                String clientDesc=orderInfo.getClientName()+" "+orderInfo.getClientSurname()+" - "+orderInfo.getFirmDescription();
                                txtClientDescription.setText(clientDesc);
                                txtDateDescription.setText(orderInfo.getDate_());

                                String orderNote = orderInfo.getOrderNote();
                                if (!orderNote.isEmpty())
                                    btnShowOrderNotePopUp.setVisibility(View.VISIBLE);

                                _currentOrderNote=orderNote;

                                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                String formattedPrice = decimalFormat.format(orderInfo.getTotalPrice());
                                txtTotalPriceDescription.setText(formattedPrice);

                                txtHasTaxDescription.setText(orderInfo.getHasTax());

                                String taxPercentage = String.valueOf(orderInfo.getTaxPercentage());

                                if (taxPercentage.equals("null") || taxPercentage.isEmpty())
                                    txtTaxPercentageDescription.setText("0");
                                else
                                    txtTaxPercentageDescription.setText(taxPercentage);

                                List<ReportOrderInformationLines> orderLines = orderInfo.getOrderLines();
                                orderInformationLinesList.clear();
                                orderInformationLinesList.addAll(orderLines);
                                orderLineInformationAdapter.notifyDataSetChanged();

                                orderLineLinearLayout.setVisibility(View.VISIBLE);
                            }
                    }
                    else {
                        Toast.makeText(ReportGeneralSalesAndPurchasing.this, "Sipariş Numarası veya Bilgilerinizi Kontrol Ediniz.", Toast.LENGTH_SHORT).show();
                        setVisibleLinearLayout();
                    }
                }

                @Override
                public void onFailure(Call<OrderInformation> call, Throwable t) {
                    Toast.makeText(ReportGeneralSalesAndPurchasing.this, "Sipariş Numarası veya Bilgilerinizi Kontrol Ediniz.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception ex){
            showDialog(ex.getMessage());
        }
    }
    private boolean validateGetData(){
        if (edtSearchOrder.getText().toString().isEmpty())
        {
            Toast.makeText(ReportGeneralSalesAndPurchasing.this, "Sipariş No Giriniz.", Toast.LENGTH_SHORT).show();
            setVisibleLinearLayout();
            return false;
        }
      return true;
    }
}