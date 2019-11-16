package com.example.admin_project.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_project.R;

class OrderDetailViewHolder extends RecyclerView.ViewHolder{
    public TextView name,quantity,price,discount;


    public OrderDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.product_name);
        quantity = itemView.findViewById(R.id.product_quantity);
        discount = itemView.findViewById(R.id.product_discount);
        price = itemView.findViewById(R.id.product_price);


    }



}
