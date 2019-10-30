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

class OrderDetailViewHolder extends RecyclerView.ViewHolder{
    public TextView productname,productquantity,productprice,productdiscount;


    public OrderDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        productname = itemView.findViewById(R.id.product_name);
        productquantity = itemView.findViewById(R.id.product_quantity);
        productdiscount = itemView.findViewById(R.id.product_discount);
        productprice = itemView.findViewById(R.id.product_price);
    }
}
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailViewHolder> {

    List<Order> myOrders;

    public OrderDetailAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview  = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_layout,parent,false);
        return  new OrderDetailViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {

        Order order  = myOrders.get(position);
        holder.productname.setText(String.format("Name : %s",order.getProductName()));
        holder.productprice.setText(String.format("Price : %s",order.getPrice()));
        holder.productdiscount.setText(String.format("Discount : %s",order.getDiscount()));
        holder.productquantity.setText(String.format("Quantity : %s",order.getQuantity()));


    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
