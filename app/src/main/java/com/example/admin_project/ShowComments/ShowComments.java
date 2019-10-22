package com.example.admin_project.ShowComments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.admin_project.Model.Rating;
import com.example.admin_project.R;
import com.example.admin_project.ViewHolder.ShowCommentsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowComments extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseRecyclerAdapter<Rating, ShowCommentsViewHolder> adapter;

    String foodId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Rating");
        recyclerView = findViewById(R.id.recycler_comments);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getComment();
    }

    private void getComment() {
        adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentsViewHolder>
                (Rating.class,R.layout.item_show_comments, ShowCommentsViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(ShowCommentsViewHolder showCommentsViewHolder, Rating rating, int i) {
                showCommentsViewHolder.txtPhone.setText(rating.getUserPhone());
                showCommentsViewHolder.txtComments.setText(rating.getComment());
                showCommentsViewHolder.txtName.setText(rating.getName());
//                showCommentsViewHolder.ratingBar.setRating(Float.parseFloat(rating.getRateValue()));

            }
        };
        recyclerView.setAdapter(adapter);
    }
}
