package com.example.admin_project.ListCustomer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.admin_project.Common.Common;
import com.example.admin_project.Interface.ItemClickListener;
import com.example.admin_project.Model.User;
import com.example.admin_project.R;
import com.example.admin_project.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Listcustomer extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<User, UserViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcustomer);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");


        recyclerView = findViewById(R.id.recycler_listcustomer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadCustomer(Common.currentUser.getPhone());
    }
    private void loadCustomer(String phone) {
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>
                (User.class,
                        R.layout.item_listcustomer,
                        UserViewHolder.class,
                        databaseReference) {
            @Override
            protected void populateViewHolder(UserViewHolder userViewHolder, final User user, int i) {
                userViewHolder.txtEmail_customer.setText(user.getEmail());
                userViewHolder.txtIsStaff.setText(user.getIsStaff());
                userViewHolder.txtPhone_customer.setText(adapter.getRef(i).getKey());
                userViewHolder.txtPaswword_customer.setText(user.getPassword());
                userViewHolder.txtName_customer.setText(user.getName());

                userViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        User userName = user;
                        Toast.makeText(Listcustomer.this, ""+userName.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
