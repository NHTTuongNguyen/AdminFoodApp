package com.example.admin_project.OrderStatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.admin_project.Common.Common;
import com.example.admin_project.R;
import com.example.admin_project.ViewHolder.OrderDetailAdapter;

public class OrderDetail extends AppCompatActivity {
    TextView order_id,order_phone,order_address,order_total;
    String order_id_value = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id = findViewById(R.id.order_id);
        order_phone = findViewById(R.id.order_phone);
        order_address = findViewById(R.id.order_address);
        order_total = findViewById(R.id.order_total);

        recyclerView = findViewById(R.id.lstFoods);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent()!=null){
            order_id_value = getIntent().getStringExtra("OrderId");
        }
        order_id.setText(order_id_value);
        order_phone.setText(Common.currentRequest.getPhone());
        order_total.setText(Common.currentRequest.getTotal());
        order_address.setText(Common.currentRequest.getAddress());

        OrderDetailAdapter adapter  = new OrderDetailAdapter(Common.currentRequest.getFoods());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);



    }
}
