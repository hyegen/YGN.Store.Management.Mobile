package com.ygn.ygn_store_management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Models.ReportViews.PurchasingDetailByClientDetail;
import com.ygn.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;

public class ReportPurchasingDetailByClientDetailAdapter extends RecyclerView.Adapter<ReportPurchasingDetailByClientDetailAdapter.ViewHolder> {
    private List<PurchasingDetailByClientDetail> purchasingDetailByClientDetailsList;
    private List<PurchasingDetailByClientDetail> filteredList;
    public ReportPurchasingDetailByClientDetailAdapter(List<PurchasingDetailByClientDetail> purchasingDetailByClientDetailsList) {
        this.purchasingDetailByClientDetailsList = purchasingDetailByClientDetailsList;
        this.filteredList = new ArrayList<>(purchasingDetailByClientDetailsList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_purchasing_by_client_detail, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchasingDetailByClientDetail purchasingDetailByClientDetail = filteredList.get(position);
        holder.orderFicheTextView.setText(purchasingDetailByClientDetail.getOrderFicheNumber());
        holder.clientNameTextView.setText(purchasingDetailByClientDetail.getClientName());
        holder.clientSurNameTextView.setText(purchasingDetailByClientDetail.getClientSurname());
        holder.firmDescriptionTextView.setText(purchasingDetailByClientDetail.getFirmDescription());
        holder.dateTextView.setText(purchasingDetailByClientDetail.getDate_());
        holder.totalPriceTextView.setText(String.valueOf(purchasingDetailByClientDetail.getTotalPrice()));
    }
    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    public void updateData(List<PurchasingDetailByClientDetail> newData) {
        purchasingDetailByClientDetailsList.clear();
        purchasingDetailByClientDetailsList.addAll(newData);
        filter("");
    }
    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(purchasingDetailByClientDetailsList);
        } else {
            text = text.toLowerCase();
            for (PurchasingDetailByClientDetail item : purchasingDetailByClientDetailsList) {
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

        public ViewHolder(@NonNull View purchasingReportView) {
            super(purchasingReportView);
            orderFicheTextView = purchasingReportView.findViewById(R.id.txtOrderFicheNumber);
            clientNameTextView = purchasingReportView.findViewById(R.id.txtClientName);
            clientSurNameTextView = purchasingReportView.findViewById(R.id.txtClientSurname);
            firmDescriptionTextView = purchasingReportView.findViewById(R.id.txtFirmDescription);
            dateTextView = purchasingReportView.findViewById(R.id.txtDate_);
            totalPriceTextView = purchasingReportView.findViewById(R.id.txtTotalPricePurchasing);
        }
    }
}