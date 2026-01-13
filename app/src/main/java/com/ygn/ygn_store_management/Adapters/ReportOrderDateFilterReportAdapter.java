package com.ygn.ygn_store_management.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ygn.ygn_store_management.Models.ReportViews.OrderFilterByDate;
import com.ygn.ygn_store_management.R;

import java.util.List;

public class ReportOrderDateFilterReportAdapter extends RecyclerView.Adapter<ReportOrderDateFilterReportAdapter.ViewHolder>
{
    List<OrderFilterByDate> list;

 public    ReportOrderDateFilterReportAdapter(List<OrderFilterByDate> list){
        this.list=list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtFiche, txtClient, txtDate, txtTotal;

        ViewHolder(View itemView) {
            super(itemView);
            txtFiche = itemView.findViewById(R.id.txtFiche);
            txtClient = itemView.findViewById(R.id.txtClient);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report_order_date_filter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        OrderFilterByDate o = list.get(position);

        h.txtFiche.setText("Fiş No: " + o.orderFicheNumber);

        String fullName = o.clientName;
        if (o.clientSurname != null)
            fullName += " " + o.clientSurname;

        h.txtClient.setText("Müşteri: " + fullName);

        h.txtDate.setText("Tarih: " + o.date);

        h.txtTotal.setText("Toplam: " + o.totalPrice + " ₺");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
