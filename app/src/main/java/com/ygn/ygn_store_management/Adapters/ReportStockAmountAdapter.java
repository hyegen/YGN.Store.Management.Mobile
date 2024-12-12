package com.ygn.ygn_store_management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Models.ReportViews.StockAmountInformation;
import com.ygn.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;

public class ReportStockAmountAdapter extends RecyclerView.Adapter<ReportStockAmountAdapter.ViewHolder> {
    private List<StockAmountInformation> stockAmountInformationList;
    private List<StockAmountInformation> filteredList;

    public ReportStockAmountAdapter(List<StockAmountInformation> stockAmountInformationList) {
        this.stockAmountInformationList = stockAmountInformationList;
        this.filteredList = new ArrayList<>(stockAmountInformationList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stock_amount, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockAmountInformation stockAmountInformation = filteredList.get(position);
        holder.itemCodeTextView.setText(stockAmountInformation.getItemCode());
        holder.itemNameTextView.setText(stockAmountInformation.getItemName());
        holder.stockAmountTextView.setText(Integer.toString(stockAmountInformation.getStockAmount()));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void updateData(List<StockAmountInformation> newData) {
        stockAmountInformationList.clear();
        stockAmountInformationList.addAll(newData);
        filter("");
    }

    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(stockAmountInformationList);
        } else {
            text = text.toLowerCase();
            for (StockAmountInformation item : stockAmountInformationList) {
                if (item.getItemCode().toLowerCase().contains(text) ||
                        item.getItemName().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemCodeTextView;
        TextView itemNameTextView;
        TextView stockAmountTextView;

        public ViewHolder(@NonNull View stockAmountInformationView) {
            super(stockAmountInformationView);
            itemCodeTextView = stockAmountInformationView.findViewById(R.id.txtItemCode);
            itemNameTextView = stockAmountInformationView.findViewById(R.id.txtItemName);
            stockAmountTextView = stockAmountInformationView.findViewById(R.id.txtStockAmount);
        }
    }
}
