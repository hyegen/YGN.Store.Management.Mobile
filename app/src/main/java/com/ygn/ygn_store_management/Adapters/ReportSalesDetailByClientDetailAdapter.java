package com.ygn.ygn_store_management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Models.ReportViews.SalesDetailByClientDetail;
import com.ygn.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;

public class ReportSalesDetailByClientDetailAdapter extends RecyclerView.Adapter<ReportSalesDetailByClientDetailAdapter.ViewHolder> {
    private List<SalesDetailByClientDetail> salesDetailByClientDetailList;
    private List<SalesDetailByClientDetail> filteredList;
    public ReportSalesDetailByClientDetailAdapter(List<SalesDetailByClientDetail> salesDetailByClientDetailList) {
        this.salesDetailByClientDetailList = salesDetailByClientDetailList;
        this.filteredList = new ArrayList<>(salesDetailByClientDetailList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sales_by_client_detail, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalesDetailByClientDetail salesDetailByClientDetail = filteredList.get(position);
        holder.orderFicheTextView.setText(salesDetailByClientDetail.getOrderFicheNumber());
        holder.clientNameTextView.setText(salesDetailByClientDetail.getClientName());
        holder.clientSurNameTextView.setText(salesDetailByClientDetail.getClientSurname());
        holder.firmDescriptionTextView.setText(salesDetailByClientDetail.getFirmDescription());
        holder.dateTextView.setText(salesDetailByClientDetail.getDate_());
        holder.totalPriceTextView.setText(String.valueOf(salesDetailByClientDetail.getTotalPrice()));
    }
    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    public void updateData(List<SalesDetailByClientDetail> newData) {
        salesDetailByClientDetailList.clear();
        salesDetailByClientDetailList.addAll(newData);
        filter("");
    }
    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(salesDetailByClientDetailList);
        } else {
            text = text.toLowerCase();
            for (SalesDetailByClientDetail item : salesDetailByClientDetailList) {
                    if (item.getClientName().toLowerCase().contains(text) ||
                            item.getOrderFicheNumber().toLowerCase().contains(text)) {
                        filteredList.add(item);
                    }
            }
        }
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderFicheTextView;
        TextView clientNameTextView;
        TextView clientSurNameTextView;
        TextView firmDescriptionTextView;
        TextView dateTextView;
        TextView totalPriceTextView;

        public ViewHolder(@NonNull View salesReportInformation) {
            super(salesReportInformation);
            orderFicheTextView = salesReportInformation.findViewById(R.id.txtId);
            clientNameTextView = salesReportInformation.findViewById(R.id.txtClientName);
            clientSurNameTextView = salesReportInformation.findViewById(R.id.txtClientSurname);
            firmDescriptionTextView = salesReportInformation.findViewById(R.id.txtFirmDescription);
            dateTextView = salesReportInformation.findViewById(R.id.txtDate_);
            totalPriceTextView = salesReportInformation.findViewById(R.id.txtTotalPriceSales);
        }
    }
}