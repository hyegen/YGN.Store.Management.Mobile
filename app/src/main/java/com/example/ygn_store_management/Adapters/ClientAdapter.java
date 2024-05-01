package com.example.ygn_store_management.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ygn_store_management.Models.Client;
import com.example.ygn_store_management.R;

import java.util.ArrayList;

public class ClientAdapter extends ArrayAdapter<Client> {
    private Context context;
    private int resource;

    public ClientAdapter(@NonNull Context context, int pResource, @NonNull ArrayList<Client> pObjects) {
        super(context, pResource, pObjects);
        this.context = context;
        this.resource=pResource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String  clientId = String.valueOf(getItem(position).ClientId);
        String clientCodeAndNameAndSurname= getItem(position).ClientCodeAndNameAndSurname;

        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.adapter_client_selection,parent,false);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        TextView txtClientId = convertView.findViewById(R.id.txtClientId);
        TextView txtClientCodeAndNameAndSurname = convertView.findViewById(R.id.txtClientCodeAndNameAndSurname);

        txtClientId.setText(clientId);
        txtClientCodeAndNameAndSurname.setText(clientCodeAndNameAndSurname);

        return convertView;
    }
}