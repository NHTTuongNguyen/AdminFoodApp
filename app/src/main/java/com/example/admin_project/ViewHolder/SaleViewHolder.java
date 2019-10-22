package com.example.admin_project.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_project.Common.Common;
import com.example.admin_project.Interface.ItemClickListener;
import com.example.admin_project.R;

public class SaleViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener {


    public TextView txtNameSale;
    public ImageView img_sale;
    private ItemClickListener itemClickListener;

    public SaleViewHolder(@NonNull View itemView) {
        super(itemView);
        txtNameSale = itemView.findViewById(R.id.txtNameSale);
        img_sale = itemView.findViewById(R.id.img_sale);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select  the action");
        menu.add(0, 1, getAdapterPosition(), Common.UPDATE);
        menu.add(0, 0, getAdapterPosition(), Common.DELETE);

    }
}
