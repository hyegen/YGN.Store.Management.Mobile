package com.example.ygn_store_management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ygn_store_management.Models.ReportViews.ReportOrderInformationLines;
import com.example.ygn_store_management.R;

import java.text.DecimalFormat;
import java.util.List;

public class ReportOrderLineInformationAdapter  extends RecyclerView.Adapter<ReportOrderLineInformationAdapter.ViewHolder> {
    private List<ReportOrderInformationLines> orderInformationLinesList;

    public ReportOrderLineInformationAdapter(List<ReportOrderInformationLines> orderInformationLinesList) {
        this.orderInformationLinesList = orderInformationLinesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_report_general_sales_and_purchasing_orderline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportOrderInformationLines orderInformationLine = orderInformationLinesList.get(position);

        double number = orderInformationLine.getLineTotal();
        String formattedNumber = String.format("%.0f", number);

            holder.itemNameTextView.setText(orderInformationLine.getItemName());
            holder.amountTextView.setText(Integer.toString(orderInformationLine.getAmount()));
            holder.unitPriceTextView.setText(Double.toString(orderInformationLine.getUnitPrice()));
            holder.lineTotalTextView.setText(formattedNumber);
    }
    @Override
    public int getItemCount() {
        return orderInformationLinesList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView amountTextView;
        TextView unitPriceTextView;
        TextView lineTotalTextView;

        public ViewHolder(@NonNull View orderLineInformationView) {
            super(orderLineInformationView);
            itemNameTextView = orderLineInformationView.findViewById(R.id.txtItemName);
            amountTextView= orderLineInformationView.findViewById(R.id.txtAmount);
            unitPriceTextView= orderLineInformationView.findViewById(R.id.txtUnitPrice);
            lineTotalTextView= orderLineInformationView.findViewById(R.id.txtLineTotal);
        }
    }
}
