package com.example.ygn_store_management.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ygn_store_management.Models.ReportViews.SalesDetailByClientDetail;
import com.example.ygn_store_management.R;

import java.util.ArrayList;

public class ReportSalesDetailByClientDetailAdapter extends ArrayAdapter<SalesDetailByClientDetail> {
    private Context context;
    private int resource;

    public ReportSalesDetailByClientDetailAdapter(@NonNull Context context, int pResource, @NonNull ArrayList<SalesDetailByClientDetail> pObjects) {
        super(context, pResource, pObjects);
        this.context = context;
        this.resource=pResource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String clientCode = getItem(position).ClientCode;
        String clientName = getItem(position).ClientName;
        String clientSurname= getItem(position).ClientSurname;
        String firmDescription= getItem(position).FirmDescription;
        String date= getItem(position).Date_;
        String totalPrice= String.valueOf(getItem(position).TotalPrice);


        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_products,parent,false);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        TextView txtClientCode = convertView.findViewById(R.id.txtClientCode);
        TextView txtClientName = convertView.findViewById(R.id.txtClientName);
        TextView txtClientSurname = convertView.findViewById(R.id.txtClientSurname);
        TextView txtFirmDescription = convertView.findViewById(R.id.txtFirmDescription);
        TextView txtDate = convertView.findViewById(R.id.txtDate_);
        TextView txtTotalPrice = convertView.findViewById(R.id.txtTotalPriceSales);

        txtClientCode.setText(clientCode);
        txtClientName.setText(clientName);
        txtClientSurname.setText(clientSurname);
        txtFirmDescription.setText(firmDescription);
        txtDate.setText(date);
        txtTotalPrice.setText(totalPrice);

        return convertView;
    }
}
