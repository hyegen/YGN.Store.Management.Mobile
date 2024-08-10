package com.example.ygn_store_management.Activities.ReportActivities.GeneralReportActivities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ygn_store_management.Adapters.ReportGeneralSalesAndPurchasingAdapter;
import com.example.ygn_store_management.Models.ReportViews.OrderInformationLines;
import com.example.ygn_store_management.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ReportGeneralSalesAndPurchasing extends AppCompatActivity {

    private static String apiUrl;
    private static final String TAG = "ReportGeneralSalesAndPurchasingActivity";
    private EditText edtSearchOrder;
    private Button btnSearchOrder;
    private Button btnShowOrderNotePopUp;
    private TextView txtOrderFicheNumberDescription;
    private TextView txtClientDescription;
    private TextView txtTotalPriceDescription;
    private TextView txtHasTaxDescription;
    private TextView txtTaxPercentageDescription;
    private TextView txtDateDescription;
    private ListView orderLineListView;
    private LinearLayout orderLineLinearLayout;
    protected ProgressDialog pleaseWait;
    public String _currentOrderNote;
    private GetOrderInformationByOrderFicheNumber _getOrderInformationByOrderFicheNumberTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_general_sales_and_purchasing);
        getSharedPreferences();
        findViews();
        events();
        initialize();
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
       // txtOrderNoteDescription=findViewById(R.id.txtOrderNoteDescription);
        txtDateDescription=findViewById(R.id.txtDateDescription);
        orderLineListView=findViewById(R.id.orderLineListView);
        orderLineLinearLayout=findViewById(R.id.orderLineLinearLayout);
        btnShowOrderNotePopUp=findViewById(R.id.btnShowOrderNotePopUp);
    }
    private void events() {
        btnSearchOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearchOrder.getText().toString().isEmpty())
                {
                    Toast.makeText(ReportGeneralSalesAndPurchasing.this, "Sipariş No Giriniz.", Toast.LENGTH_SHORT).show();
                    setVisibleLinearLayout();
                }
                else{
                    //new GetOrderInformationByOrderFicheNumber().execute(edtSearchOrder.getText().toString());
                    _getOrderInformationByOrderFicheNumberTask = new GetOrderInformationByOrderFicheNumber();
                    _getOrderInformationByOrderFicheNumberTask.execute(edtSearchOrder.getText().toString());
                }
            }
        });
        edtSearchOrder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                   // new GetOrderInformationByOrderFicheNumber().execute(edtSearchOrder.getText().toString());
                    _getOrderInformationByOrderFicheNumberTask = new GetOrderInformationByOrderFicheNumber();
                    _getOrderInformationByOrderFicheNumberTask.execute(edtSearchOrder.getText().toString());
                    return true;
                }
                return false;
            }
        });
        btnShowOrderNotePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog( _currentOrderNote);
            }
        });
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
    private class GetOrderInformationByOrderFicheNumber extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pleaseWait = ProgressDialog.show(ReportGeneralSalesAndPurchasing.this, ReportGeneralSalesAndPurchasing.this.getResources().getString(R.string.loading), ReportGeneralSalesAndPurchasing.this.getResources().getString(R.string.please_wait));
        }
        @Override
        protected String doInBackground(String... params) {
            String orderFicheNumber = params[0];
            String apiRoute = "/api/GetOrderDetailInformation";
            try {
                URL url = new URL(apiUrl + apiRoute + "?orderFicheNumber=" + orderFicheNumber);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } catch (Exception e) {
                Log.e(TAG, "Hata: " + e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPostExecute(String jsonData) {
            if (pleaseWait != null) {
                pleaseWait.dismiss();
            }
            if(jsonData.equals("null")){
                Toast.makeText(ReportGeneralSalesAndPurchasing.this, edtSearchOrder.getText()+" "+"Sipariş Bulunamadı. \nSipariş Numarasını kontrol ediniz", Toast.LENGTH_SHORT).show();
                edtSearchOrder.setText("");
                setVisibleLinearLayout();
            }
            if (jsonData != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonData);

                    String orderFicheNumber = jsonResponse.getString("OrderFicheNumber");
                    String clientName = jsonResponse.getString("ClientName");
                    String clientSurname = jsonResponse.getString("ClientSurname");
                    String firmDescription = jsonResponse.getString("FirmDescription");
                    String date_ = jsonResponse.getString("Date_");
                    String orderNote = jsonResponse.getString("OrderNote");
                    String totalPrice = String.valueOf(jsonResponse.getLong("TotalPrice"));
                    String hasTax = jsonResponse.getString("HasTax");

                    if (!orderNote.isEmpty())
                        btnShowOrderNotePopUp.setVisibility(View.VISIBLE);

                    txtClientDescription.setText(clientName + " "+clientSurname+ " - " +firmDescription);
                    txtOrderFicheNumberDescription.setText(orderFicheNumber);
                    //txtOrderNoteDescription.setText(orderNote);
                    txtDateDescription.setText(date_);
                    txtTotalPriceDescription.setText(totalPrice);
                    txtHasTaxDescription.setText(hasTax);

                   String taxPercentage = String.valueOf(jsonResponse.getDouble("TaxPercentage"));

                   if (taxPercentage.equals("null") || taxPercentage.isEmpty())
                        txtTaxPercentageDescription.setText("0");
                   else
                       txtTaxPercentageDescription.setText(taxPercentage);

                    JSONArray jsonArray = jsonResponse.getJSONArray("OrderLines");
                    ArrayList<OrderInformationLines> orderLines = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        OrderInformationLines orderLine = new OrderInformationLines();
                        orderLine.setItemName(item.getString("ItemName"));
                        orderLine.setAmount(item.getInt("Amount"));
                        orderLine.setUnitPrice(item.getDouble("UnitPrice"));
                        orderLine.setLineTotal(item.getInt("LineTotal"));
                        orderLines.add(orderLine);
                    }

                    ReportGeneralSalesAndPurchasingAdapter adapter = new ReportGeneralSalesAndPurchasingAdapter(ReportGeneralSalesAndPurchasing.this, R.layout.adapter_report_general_sales_and_purchasing_orderline, orderLines);

                    orderLineListView.setAdapter(adapter);
                    orderLineLinearLayout.setVisibility(View.VISIBLE);

                    _currentOrderNote=orderNote;

                } catch (JSONException e) {
                    Log.e(TAG, "JSON Hatası: " + e.getMessage());
                }
                edtSearchOrder.setText("");
            }
            else  {
                setVisibleLinearLayout();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (_getOrderInformationByOrderFicheNumberTask != null && !_getOrderInformationByOrderFicheNumberTask.isCancelled()) {
            _getOrderInformationByOrderFicheNumberTask.cancel(true);
        }

        btnSearchOrder.setOnClickListener(null);
        edtSearchOrder.setOnEditorActionListener(null);
        txtOrderFicheNumberDescription.setText(null);
        txtClientDescription.setText(null);
        txtTotalPriceDescription.setText(null);
        txtHasTaxDescription.setText(null);
        txtTaxPercentageDescription.setText(null);
        txtDateDescription.setText(null);
        orderLineListView.setAdapter(null);

        orderLineLinearLayout.removeAllViews();
        btnShowOrderNotePopUp.setOnClickListener(null);

        if(apiUrl!=null)
            apiUrl=null;

        if(_currentOrderNote!=null)
            _currentOrderNote=null;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    private void clearItems(){

    }
}