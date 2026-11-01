package com.ygn.ygn_store_management.Adapters;


import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Models.Item;
import com.ygn.ygn_store_management.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SalesItemAdapter
        extends RecyclerView.Adapter<SalesItemAdapter.Holder> {

    private final List<Item> items;
    private final Map<Integer, Integer> amounts;
    private final Map<Integer, Double> unitPrices;
    private final Runnable onChanged;

    public SalesItemAdapter(
            List<Item> items,
            Map<Integer, Integer> amounts,
            Map<Integer, Double> unitPrices,
            Runnable onChanged) {

        this.items = items;
        this.amounts = amounts;
        this.unitPrices = unitPrices;
        this.onChanged = onChanged;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView txtName, txtCode, txtTotal;
        EditText etAmount, etUnitPrice;
        ImageButton btnDelete;

        TextWatcher amountWatcher;
        TextWatcher priceWatcher;

        public Holder(View view) {
            super(view);

            txtName = view.findViewById(R.id.txtItemName);
            txtCode = view.findViewById(R.id.txtItemCode);
            txtTotal = view.findViewById(R.id.txtLineTotal);

            etAmount = view.findViewById(R.id.etAmount);
            etUnitPrice = view.findViewById(R.id.etUnitPrice);

            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public Holder onCreateViewHolder(
            ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sales_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(
                    Color.parseColor("#FFFFFF")
            );
        } else {
            holder.itemView.setBackgroundColor(
                    Color.parseColor("#D0D0D0")
            );
        }
        Item item = items.get(position);
        int itemId = item.getId();

        holder.txtName.setText(item.getItemName());
        holder.txtCode.setText(item.getItemCode());

        // Eski listener'ları kaldır
        if (holder.amountWatcher != null) {
            holder.etAmount.removeTextChangedListener(
                    holder.amountWatcher);
        }

        if (holder.priceWatcher != null) {
            holder.etUnitPrice.removeTextChangedListener(
                    holder.priceWatcher);
        }

        int amount = amounts.containsKey(itemId)
                ? amounts.get(itemId) : 1;

        double price = unitPrices.containsKey(itemId)
                ? unitPrices.get(itemId) : 0;

        holder.etAmount.setText(String.valueOf(amount));
        //holder.etUnitPrice.setText(String.valueOf(price));

        DecimalFormat decimalFormat = new DecimalFormat(
                "0.##",
                new DecimalFormatSymbols(new Locale("tr", "TR"))
        );

        holder.etUnitPrice.setText(
                decimalFormat.format(price)
        );

        updateLineTotal(holder, amount, price);

        // Miktar değişikliği
        holder.amountWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {

                int value = 0;

                try {
                    value = Integer.parseInt(s.toString());
                } catch (NumberFormatException ignored) {}

                amounts.put(itemId, value);

                double currentPrice =
                        unitPrices.containsKey(itemId)
                                ? unitPrices.get(itemId) : 0;

                updateLineTotal(holder, value, currentPrice);
                onChanged.run();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        holder.etAmount.addTextChangedListener(
                holder.amountWatcher);

        // Birim fiyat değişikliği
        holder.priceWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {

                double value = 0;

                try {
                    value = Double.parseDouble(
                            s.toString().replace(",", "."));
                } catch (NumberFormatException ignored) {}

                unitPrices.put(itemId, value);

                int currentAmount =
                        amounts.containsKey(itemId)
                                ? amounts.get(itemId) : 0;

                updateLineTotal(holder, currentAmount, value);
                onChanged.run();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        holder.etUnitPrice.addTextChangedListener(
                holder.priceWatcher);

        // Ürün silme
        holder.btnDelete.setOnClickListener(v -> {

            int index = holder.getAdapterPosition();

            if (index == RecyclerView.NO_POSITION) {
                return;
            }

            Item removed = items.remove(index);

            amounts.remove(removed.getId());
            unitPrices.remove(removed.getId());

            notifyItemRemoved(index);
            notifyItemRangeChanged(index, items.size() - index);
            onChanged.run();
        });
    }

    private void updateLineTotal(
            Holder holder, int amount, double price) {

        double total = amount * price;

        holder.txtTotal.setText(String.format(
                new Locale("tr", "TR"),
                "%,.2f TL", total));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

