package com.example.admin_project.ViewHolder;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_project.Model.Order;
import com.example.admin_project.R;

import java.util.List;


public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailViewHolder> {

    List<Order> myOrders;

    public OrderDetailAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout,parent,false);
        return  new OrderDetailViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {

        Order order  = myOrders.get(position);
//        holder.name.setText(String.format("Name : %s",order.getProductName()));
//        holder.price.setText(String.format("Price : %s",order.getPrice()));
//        holder.discount.setText(String.format("Discount : %s",order.getDiscount()));
//        holder.quantity.setText(String.format("Quantity : %s",order.getQuantity()));

        holder.name.setText(String.format(order.getProductName()));
        holder.price.setText(String.format(order.getPrice()));
        holder.discount.setText(String.format(order.getDiscount()));
        holder.quantity.setText(String.format(order.getQuantity()));


    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
