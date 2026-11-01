
package com.ygn.ygn_store_management.Activities.SalesActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Activities.DialogActivities.SalesAmountDialogActivity;
import com.ygn.ygn_store_management.Adapters.ItemSelectionAdapter;
import com.ygn.ygn_store_management.Adapters.SalesItemAdapter;
import com.ygn.ygn_store_management.Interfaces.ClientService;
import com.ygn.ygn_store_management.Interfaces.ItemSelectionService;
import com.ygn.ygn_store_management.Interfaces.ItemService;
import com.ygn.ygn_store_management.Interfaces.OrderService;
import com.ygn.ygn_store_management.Managers.ApiUtils;
import com.ygn.ygn_store_management.Models.Client;
import com.ygn.ygn_store_management.Models.Dtos.ItemSelectionDto;
import com.ygn.ygn_store_management.Models.Item;
import com.ygn.ygn_store_management.Models.Order;
import com.ygn.ygn_store_management.Models.OrderLine;
import com.ygn.ygn_store_management.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SalesActivity extends AppCompatActivity {
    private String _currentToken;
    private Integer _selectedClientId;
    private String _clientCodeAndNameAndSurname;
    private Integer _ioCode;
    private static String apiUrl;
    private Spinner spinnerClients;
    private List<Client> clientList = new ArrayList<>();
    private ArrayAdapter<Client> clientAdapter;
    private EditText etSearch;
    private Button btnSearch, btnDate, btnCompleteOrder;
    private TextView txtTotal;
    private RecyclerView recyclerViewItems;
    private final List<Item> itemList = new ArrayList<>();
    private final Map<Integer, Integer> amounts = new HashMap<>();
    private TextView txtOrderFicheNumber;
    private String _orderFicheNumber = null;
    private final Map<Integer, Double> unitPrices = new HashMap<>();
    private SalesItemAdapter adapter;
    private Calendar selectedDate = Calendar.getInstance();
    private boolean isSaving = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        getSharedPreferences();
        getExtras();
        findViews();

        initialize();
        events();
        fetchClients();
        fetchOrderFicheNumber();
    }
    private void getSharedPreferences() {
        SharedPreferences prefs =
                getSharedPreferences("MY_PREFS", MODE_PRIVATE);

        String ip = prefs.getString("ipAddress", "");
        apiUrl = "https://" + ip;
    }
    private void getExtras() {
        _currentToken = getIntent().getStringExtra("TOKEN");

        _selectedClientId =
                getIntent().getIntExtra("selectedClientId", -1);

        _clientCodeAndNameAndSurname =
                getIntent().getStringExtra(
                        "ClientCodeAndNameAndSurname");

        _ioCode = getIntent().getIntExtra("IOCode", -1);
    }
    private void findViews() {
        spinnerClients = findViewById(R.id.spinnerClients);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btnSearch);
        btnDate = findViewById(R.id.btnDate);
        txtTotal = findViewById(R.id.txtTotal);
        recyclerViewItems = findViewById(R.id.rv_products);
        btnCompleteOrder = findViewById(R.id.btn_complete_order);
    }
    private void initialize() {
        txtOrderFicheNumber =
                findViewById(R.id.txtOrderFicheNumber);

        adapter = new SalesItemAdapter(
                itemList,
                amounts,
                unitPrices,
                this::updateTotal
        );


        recyclerViewItems.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewItems.setAdapter(adapter);

        etSearch.setSingleLine(true);

        etSearch.setImeOptions(
                EditorInfo.IME_ACTION_SEARCH
        );

        updateDateButton();


    }
    private void events() {

        btnSearch.setOnClickListener(v -> searchItem());

        etSearch.setOnEditorActionListener(
                (v, actionId, event) -> {

                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        searchItem();
                        return true;
                    }
                    return false;
                });

        btnDate.setOnClickListener(v -> selectDate());

        btnCompleteOrder.setOnClickListener(v -> completeOrder());



        spinnerClients.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        Client client =
                                (Client) parent.getItemAtPosition(position);

                        _selectedClientId = client.getId();

                        _clientCodeAndNameAndSurname =
                                client.getNameAndSurname();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        _selectedClientId = -1;
                    }
                });

        btnCompleteOrder.setOnClickListener(v -> completeOrder());
    }
    private void searchItem() {

        String itemCode =
                etSearch.getText().toString().trim();

        if (itemCode.isEmpty()) {
            etSearch.setError("Ürün kodu giriniz");
            return;
        }

        Retrofit retrofit =
                ApiUtils.InitRequestWithoutToken(apiUrl);

        ItemService service =
                retrofit.create(ItemService.class);

        service.getItemByItemCode(itemCode)
                .enqueue(new Callback<Item>() {

                    @Override
                    public void onResponse(
                            Call<Item> call,
                            Response<Item> response) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(SalesActivity.this,
                                    "Ürün bulunamadı",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        addItem(response.body());
                    }

                    @Override
                    public void onFailure(
                            Call<Item> call, Throwable t) {

                        Toast.makeText(SalesActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void addItem(Item item) {

        for (Item existing : itemList) {

            if (existing.getId() == item.getId()) {

                Toast.makeText(this,
                        "Bu ürün zaten listede!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        itemList.add(item);

        int amount = item.getPackageAmount() > 0
                ? item.getPackageAmount() : 1;

        amounts.put(item.getId(), amount);

        Double defaultPrice = (_ioCode != null && _ioCode == 1)
                ? item.getPurchasingPrice()
                : item.getSalesPrice();

        unitPrices.put(
                item.getId(),
                defaultPrice == null ? 0 : defaultPrice
        );

        adapter.notifyItemInserted(itemList.size() - 1);

        recyclerViewItems.scrollToPosition(
                itemList.size() - 1);

        etSearch.setText("");
        etSearch.requestFocus();

        updateTotal();
    }
    private double getUnitPrice(Item item) {

        Double price = (_ioCode != null && _ioCode == 1)
                ? item.getPurchasingPrice()
                : item.getSalesPrice();

        return price == null ? 0 : price;
    }
    private void updateTotal() {

        double total = 0;

        for (Item item : itemList) {

            int amount = amounts.containsKey(item.getId())
                    ? amounts.get(item.getId()) : 0;

            double price = unitPrices.containsKey(item.getId())
                    ? unitPrices.get(item.getId()) : 0;

            total += amount * price;
        }

        txtTotal.setText(String.format(
                new Locale("tr", "TR"),
                "Toplam: %,.2f TL", total));
    }
    private void selectDate() {

        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    selectedDate.set(
                            Calendar.YEAR, selectedYear);

                    selectedDate.set(
                            Calendar.MONTH, selectedMonth);

                    selectedDate.set(
                            Calendar.DAY_OF_MONTH, selectedDay);

                    SimpleDateFormat displayFormat =
                            new SimpleDateFormat(
                                    "dd.MM.yyyy",
                                    new Locale("tr", "TR")
                            );

                    btnDate.setText(
                            displayFormat.format(selectedDate.getTime())
                    );
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }
    private void updateDateButton() {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "dd.MM.yyyy", Locale.getDefault());

        btnDate.setText(format.format(
                selectedDate.getTime()));
    }
    private void completeOrder() {

        if (isSaving) return;
        if (_orderFicheNumber == null
                || _orderFicheNumber.isEmpty()) {

            Toast.makeText(
                    this,
                    "Fiş numarası henüz alınamadı",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        if (_selectedClientId == null || _selectedClientId <= 0) {
            Toast.makeText(this, "Cari seçiniz",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemList.isEmpty()) {
            Toast.makeText(this, "En az bir ürün ekleyiniz",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        SimpleDateFormat apiDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss",
                Locale.US
        );

        String orderDate = apiDateFormat.format(
                selectedDate.getTime()
        );

        Order order = new Order();
        order.OrderNumber = _orderFicheNumber;

        order.CreatedDate_ = orderDate;
        order.ClientId = _selectedClientId;
        order.IOCode = 2;
        order.TransactionCode = 4;



        // Geçici değerler: gerçek kullanıcı ve ödeme
        // bilgilerine göre güncellenecek.
        order.OrderCreatedUserId = 1;
        order.PaymentType = 1;

        order.HasTax = false;
        order.TaxPercentage = 0;
        order.isCompleted = false;
        order.OrderNote = null;

        order.OrderLines = new ArrayList<>();

        double total = 0;

        for (Item item : itemList) {

            int amount = amounts.containsKey(item.getId())
                    ? amounts.get(item.getId()) : 0;

            Double unitPrice = unitPrices.get(item.getId());

            if (amount <= 0) {
                Toast.makeText(this,
                        "Geçersiz miktar: " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (unitPrice == null
                    || !Double.isFinite(unitPrice)
                    || unitPrice < 0) {

                Toast.makeText(this,
                        "Geçersiz fiyat: " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            OrderLine line = new OrderLine();

            line.ItemId = item.getId();
            line.Amount = amount;
            line.UnitPrice = unitPrice;
            line.LineTotal = amount * unitPrice;
            line.IOCode = 2;
            line.TransactionCode = 4;

            line.CreatedDate_ = orderDate;
            order.OrderLines.add(line);

            total += line.LineTotal;
        }

        order.TotalPrice = total;

        saveOrder(order);
    }
    private void saveOrder(Order order) {

        Retrofit retrofit =
                ApiUtils.InitRequestWithToken(
                        apiUrl, _currentToken);

        OrderService service =
                retrofit.create(OrderService.class);

        isSaving = true;
        btnCompleteOrder.setEnabled(false);

        service.saveOrder(_currentToken,order)
                .enqueue(new Callback<ResponseBody>() {

                    /*@Override
                    public void onResponse(
                            Call<ResponseBody> call,
                            Response<ResponseBody> response) {

                        isSaving = false;
                        btnCompleteOrder.setEnabled(true);

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    SalesActivity.this,
                                    "Sipariş başarıyla kaydedildi",
                                    Toast.LENGTH_LONG).show();

                            setResult(RESULT_OK);
                            finish();

                        } else {

                            Toast.makeText(
                                    SalesActivity.this,
                                    "Kayıt hatası: " + response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
*/
                    @Override
                    public void onResponse(
                            Call<ResponseBody> call,
                            Response<ResponseBody> response) {

                        isSaving = false;
                        btnCompleteOrder.setEnabled(true);

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    SalesActivity.this,
                                    "Sipariş başarıyla kaydedildi",
                                    Toast.LENGTH_LONG
                            ).show();

                            setResult(RESULT_OK);
                            finish();

                        } else {

                            String errorMessage = "";

                            try {
                                if (response.errorBody() != null) {
                                    errorMessage = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                errorMessage = e.getMessage();
                            }

                            Log.e("ORDER_SAVE",
                                    "HTTP: " + response.code()
                                            + " | Hata: " + errorMessage);

                            Toast.makeText(
                                    SalesActivity.this,
                                    "Kayıt hatası: " + response.code()
                                            + "\n" + errorMessage,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ResponseBody> call,
                            Throwable t) {

                        isSaving = false;
                        btnCompleteOrder.setEnabled(true);

                        Toast.makeText(
                                SalesActivity.this,
                                "Bağlantı hatası: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void fetchClients() {

        Retrofit retrofit =
                ApiUtils.InitRequestWithToken(apiUrl, _currentToken);

        ClientService service =
                retrofit.create(ClientService.class);

        service.getClients(_currentToken)
                .enqueue(new Callback<List<Client>>() {

                    @Override
                    public void onResponse(
                            Call<List<Client>> call,
                            Response<List<Client>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            clientList.clear();
                            clientList.addAll(response.body());

                            // Türkçe alfabetik sıralama (A-Z)
                            Collator collator = Collator.getInstance(
                                    new Locale("tr", "TR")
                            );

                            Collections.sort(clientList, (c1, c2) ->
                                    collator.compare(
                                            c1.getNameAndSurname() == null
                                                    ? ""
                                                    : c1.getNameAndSurname(),
                                            c2.getNameAndSurname() == null
                                                    ? ""
                                                    : c2.getNameAndSurname()
                                    )
                            );

                            clientAdapter = new ArrayAdapter<>(
                                    SalesActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    clientList
                            );

                            clientAdapter.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item
                            );

                            spinnerClients.setAdapter(clientAdapter);

                            // Intent ile gelen cari varsa seç
                            if (_selectedClientId != null
                                    && _selectedClientId > 0) {

                                for (int i = 0; i < clientList.size(); i++) {

                                    if (clientList.get(i).getId()
                                            == _selectedClientId) {

                                        spinnerClients.setSelection(i);
                                        break;
                                    }
                                }
                            }

                        } else {

                            Toast.makeText(
                                    SalesActivity.this,
                                    "Cariler getirilemedi: " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Client>> call,
                            Throwable t) {

                        Toast.makeText(
                                SalesActivity.this,
                                "Bağlantı hatası: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
    private void fetchOrderFicheNumber() {

        Retrofit retrofit =
                ApiUtils.InitRequestWithToken(
                        apiUrl, _currentToken);

        OrderService service =
                retrofit.create(OrderService.class);

        service.getOrderFicheNumber(_currentToken)
                .enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(
                            Call<String> call,
                            Response<String> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            String ficheNumber =
                                    response.body().trim();

                            if (!ficheNumber.matches("\\d{6}")) {

                                Toast.makeText(
                                        SalesActivity.this,
                                        "Geçersiz fiş numarası",
                                        Toast.LENGTH_LONG
                                ).show();
                                return;
                            }

                            _orderFicheNumber = ficheNumber;

                            txtOrderFicheNumber.setText(
                                    "Fiş No: " + _orderFicheNumber
                            );

                        } else {

                            Toast.makeText(
                                    SalesActivity.this,
                                    "Fiş numarası alınamadı: "
                                            + response.code(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<String> call,
                            Throwable t) {

                        Toast.makeText(
                                SalesActivity.this,
                                "Fiş numarası hatası: "
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }


}
