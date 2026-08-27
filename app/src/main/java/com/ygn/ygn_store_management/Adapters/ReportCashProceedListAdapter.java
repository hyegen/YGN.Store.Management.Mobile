package com.ygn.ygn_store_management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Models.ReportViews.ReportCashProceedListViews;
import com.ygn.ygn_store_management.R;

import java.util.ArrayList;
import java.util.List;

public class ReportCashProceedListAdapter extends RecyclerView.Adapter<ReportCashProceedListAdapter.ViewHolder> {
    private List<ReportCashProceedListViews> _reportCashProceedListViewList;
    private List<ReportCashProceedListViews> filteredList;
    public ReportCashProceedListAdapter(List<ReportCashProceedListViews> reportCashProceedListViewList ) {
        this._reportCashProceedListViewList = reportCashProceedListViewList;
        this.filteredList = new ArrayList<>(reportCashProceedListViewList);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cash_proceed_list_view, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportCashProceedListViews reportCashProceedListViews = filteredList.get(position);
        holder.cashProceedIdTextView.setText(String.valueOf(reportCashProceedListViews.getCashProceedId()));
        holder.clientNameTextView.setText(reportCashProceedListViews.getClientDesc());
        holder.cashProceedTypeTextView.setText(reportCashProceedListViews.getCashProceedType());
        holder.firmDescriptionTextView.setText(reportCashProceedListViews.getFirmDescription());
        holder.dateTextView.setText(reportCashProceedListViews.getCashProceedDate_());
        holder.totalPriceTextView.setText(String.valueOf(reportCashProceedListViews.getCashProceedAmount()));
    }
    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    public void updateData(List<ReportCashProceedListViews> newData) {
        _reportCashProceedListViewList.clear();
        _reportCashProceedListViewList.addAll(newData);
        filter("");
    }
    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(_reportCashProceedListViewList);
        } else {
            text = text.toLowerCase();
            for (ReportCashProceedListViews item : _reportCashProceedListViewList) {
                if (item.getClientDesc().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cashProceedIdTextView;
        TextView clientNameTextView;
        TextView cashProceedTypeTextView;
        TextView firmDescriptionTextView;
        TextView dateTextView;
        TextView totalPriceTextView;

        public ViewHolder(@NonNull View salesReportInformation) {
            super(salesReportInformation);
            cashProceedIdTextView = salesReportInformation.findViewById(R.id.txtCashProceedId);
            clientNameTextView = salesReportInformation.findViewById(R.id.txtClientName);
            cashProceedTypeTextView = salesReportInformation.findViewById(R.id.txtCashProceedType);
            firmDescriptionTextView = salesReportInformation.findViewById(R.id.txtFirmDescription);
            dateTextView = salesReportInformation.findViewById(R.id.txtDate_);
            totalPriceTextView = salesReportInformation.findViewById(R.id.txtTotalPriceSales);
        }
    }
}
