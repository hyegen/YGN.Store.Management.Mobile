
package com.ygn.ygn_store_management.Activities.SalesActivities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Adapters.SalesItemAdapter;
import com.ygn.ygn_store_management.Interfaces.ClientService;
import com.ygn.ygn_store_management.Interfaces.ItemService;
import com.ygn.ygn_store_management.Interfaces.OrderService;
import com.ygn.ygn_store_management.Managers.ApiUtils;
import com.ygn.ygn_store_management.Models.Client;
import com.ygn.ygn_store_management.Models.Item;
import com.ygn.ygn_store_management.Models.Order;
import com.ygn.ygn_store_management.Models.OrderLine;
import com.ygn.ygn_store_management.R;

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


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Date;

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
    @Override
    public void onConfigurationChanged(
            @NonNull Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
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
        order.setOrderNumber(_orderFicheNumber);

        order.setCreatedDate_(orderDate);
        order.setClientId(_selectedClientId);
        order.setIOCode(2);
        order.setTransactionCode(4);



        // Geçici değerler: gerçek kullanıcı ve ödeme
        // bilgilerine göre güncellenecek.
        order.setOrderCreatedUserId(1);
        order.setPaymentType(1);

        order.setHasTax(false);
        order.setTaxPercentage(0);
        order.setCompleted(false);
        order.setOrderNote(null);

        order.setOrderLines(new ArrayList<>());

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
            order.getOrderLines().add(line);

            total += line.LineTotal;
        }

        order.setTotalPrice(total);

       // saveOrder(order);
        showSaveDialog(order);
    }
/*    private void saveOrder(Order order) {

        Retrofit retrofit =
                ApiUtils.InitRequestWithToken(
                        apiUrl, _currentToken);

        OrderService service =
                retrofit.create(OrderService.class);

        isSaving = true;
        btnCompleteOrder.setEnabled(false);

        service.saveOrder(_currentToken,order)
                .enqueue(new Callback<ResponseBody>() {

                    *//*@Override
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
*//*
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
    }*/





    private void saveOrder(Order order, boolean printAfterSave) {

        if (isSaving) return;

        Retrofit retrofit =
                ApiUtils.InitRequestWithToken(apiUrl, _currentToken);

        OrderService service =
                retrofit.create(OrderService.class);

        isSaving = true;
        btnCompleteOrder.setEnabled(false);

        service.saveOrder(_currentToken, order)
                .enqueue(new Callback<ResponseBody>() {

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

                            if (printAfterSave) {

                                // PDF oluşturma ve yazdırma
                                // işlemini buraya ekleyeceğiz.
                                printSavedOrder(order);

                            } else {
                                finish();
                            }

                        } else {

                            String errorMessage = "";

                            try {
                                if (response.errorBody() != null) {
                                    errorMessage =
                                            response.errorBody().string();
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
                                Toast.LENGTH_LONG
                        ).show();
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


    private void showSaveDialog(Order order) {

        if (isSaving) return;

        String[] options = {
                "Kaydet",
                "Kaydet ve Yazdır"
        };

        new AlertDialog.Builder(this)
                .setTitle("Sipariş İşlemi")
                .setItems(options, (dialog, which) -> {

                    if (which == 0) {
                        saveOrder(order, false);
                    } else if (which == 1) {
                        saveOrder(order, true);
                    }

                })
                .setNegativeButton("İptal", null)
                .show();
    }


    private File createOrderPdf(Order order) throws IOException {

        PdfDocument document = new PdfDocument();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(10);

        int pageWidth = 595;
        int pageHeight = 842;
        int margin = 35;
        int pageNumber = 1;

        NumberFormat money = NumberFormat.getNumberInstance(
                new Locale("tr", "TR"));
        money.setMinimumFractionDigits(2);
        money.setMaximumFractionDigits(2);

        try {

            PdfDocument.Page page = document.startPage(
                    new PdfDocument.PageInfo.Builder(
                            pageWidth, pageHeight, pageNumber).create());

            Canvas canvas = page.getCanvas();
            int y = margin;

            // BAŞLIK
            paint.setTextSize(18);
            paint.setFakeBoldText(true);
            canvas.drawText("SİPARİŞ FİŞİ", 205, y + 20, paint);

            paint.setTextSize(10);
            paint.setFakeBoldText(false);

            y += 60;

            canvas.drawText(
                    "Fiş No: " + order.getOrderNumber(),
                    margin, y, paint);

            String date = new SimpleDateFormat(
                    "dd.MM.yyyy",
                    new Locale("tr", "TR"))
                    .format(selectedDate.getTime());

            canvas.drawText(
                    "Tarih: " + date,
                    400, y, paint);

            y += 25;

            String clientName =
                    _clientCodeAndNameAndSurname == null
                            ? ""
                            : _clientCodeAndNameAndSurname;

            canvas.drawText(
                    "Cari: " + clientName,
                    margin, y, paint);

            y += 30;

            // TABLO BAŞLIĞI
            paint.setFakeBoldText(true);

            canvas.drawLine(margin, y, 560, y, paint);

            y += 20;

            canvas.drawText("ÜRÜN", 35, y, paint);
            canvas.drawText("MİKTAR", 320, y, paint);
            canvas.drawText("FİYAT", 405, y, paint);
            canvas.drawText("TOPLAM", 490, y, paint);

            paint.setFakeBoldText(false);

            y += 10;
            canvas.drawLine(margin, y, 560, y, paint);

            double generalTotal = 0;

            // ÜRÜNLER
            for (Item item : itemList) {

                int amount = amounts.containsKey(item.getId())
                        ? amounts.get(item.getId()) : 0;

                double unitPrice =
                        unitPrices.containsKey(item.getId())
                                ? unitPrices.get(item.getId()) : 0;

                double lineTotal = amount * unitPrice;
                generalTotal += lineTotal;

                // Sayfa dolduysa yeni sayfa
                if (y + 35 > pageHeight - 80) {

                    document.finishPage(page);
                    pageNumber++;

                    page = document.startPage(
                            new PdfDocument.PageInfo.Builder(
                                    pageWidth, pageHeight, pageNumber)
                                    .create());

                    canvas = page.getCanvas();
                    y = margin;

                    paint.setFakeBoldText(true);
                    canvas.drawText(
                            "SİPARİŞ FİŞİ - DEVAM",
                            margin, y, paint);
                    paint.setFakeBoldText(false);

                    y += 30;
                }

                y += 25;

                String itemName = item.getItemName() == null
                        ? ""
                        : item.getItemName();

                // Ürün adı çok uzunsa kes
                itemName = itemName.substring(
                        0, Math.min(itemName.length(), 43));

                canvas.drawText(
                        itemName, 35, y, paint);

                canvas.drawText(
                        String.valueOf(amount),
                        320, y, paint);

                canvas.drawText(
                        money.format(unitPrice),
                        405, y, paint);

                canvas.drawText(
                        money.format(lineTotal),
                        490, y, paint);

                y += 10;
                canvas.drawLine(margin, y, 560, y, paint);
            }

            // GENEL TOPLAM
            if (y + 45 > pageHeight - margin) {

                document.finishPage(page);
                pageNumber++;

                page = document.startPage(
                        new PdfDocument.PageInfo.Builder(
                                pageWidth, pageHeight, pageNumber)
                                .create());

                canvas = page.getCanvas();
                y = margin;
            }

            y += 35;

            paint.setFakeBoldText(true);
            paint.setTextSize(12);

            canvas.drawText(
                    "GENEL TOPLAM: " + money.format(generalTotal) + " TL",
                    320, y, paint);

            document.finishPage(page);

            File pdfFile = new File(
                    getCacheDir(),
                    "Siparis_" + order.getOrderNumber() + ".pdf");

            try (FileOutputStream output =
                         new FileOutputStream(pdfFile)) {

                document.writeTo(output);
            }

            return pdfFile;

        } finally {
            document.close();
        }
    }



    private void printPdf(File pdfFile) {

        PrintManager printManager =
                (PrintManager) getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter adapter = new PrintDocumentAdapter() {

            @Override
            public void onLayout(
                    PrintAttributes oldAttributes,
                    PrintAttributes newAttributes,
                    CancellationSignal cancellationSignal,
                    LayoutResultCallback callback,
                    Bundle extras) {

                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }

                PrintDocumentInfo info =
                        new PrintDocumentInfo.Builder(pdfFile.getName())
                                .setContentType(
                                        PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                                .build();

                callback.onLayoutFinished(info, true);
            }

            @Override
            public void onWrite(
                    PageRange[] pages,
                    ParcelFileDescriptor destination,
                    CancellationSignal cancellationSignal,
                    WriteResultCallback callback) {

                try (FileInputStream input =
                             new FileInputStream(pdfFile);
                     FileOutputStream output =
                             new FileOutputStream(
                                     destination.getFileDescriptor())) {

                    byte[] buffer = new byte[8192];
                    int length;

                    while ((length = input.read(buffer)) != -1) {

                        if (cancellationSignal.isCanceled()) {
                            callback.onWriteCancelled();
                            return;
                        }

                        output.write(buffer, 0, length);
                    }

                    output.flush();
                    callback.onWriteFinished(
                            new PageRange[]{PageRange.ALL_PAGES});

                } catch (Exception e) {
                    callback.onWriteFailed(e.getMessage());
                }
            }
        };

        PrintAttributes attributes =
                new PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .build();

        printManager.print("Sipariş Fişi", adapter, attributes);
    }


    private void printSavedOrder(Order order) {

        try {
            File pdfFile = createOrderPdf(order);

            printPdf(pdfFile);

        } catch (Exception e) {

            Log.e("PDF_PRINT", "PDF oluşturulamadı", e);

            Toast.makeText(
                    this,
                    "Sipariş kaydedildi fakat PDF oluşturulamadı: "
                            + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }


}
