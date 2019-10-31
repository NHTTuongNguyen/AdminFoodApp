package com.example.admin_project.FoodDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.admin_project.Common.Common;
import com.example.admin_project.Model.Food;
import com.example.admin_project.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {
    TextView tv_name,tv_price,tv_description;
    ImageView food_image,img_feedback;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton_Cart;
    ElegantNumberButton numberButton;
    String foodId = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Food currentFood;
    Button button_buy;
    DatabaseReference ratingTb1;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);



        tv_name = findViewById(R.id.txt_Food_Detail_Name);
        tv_price = findViewById(R.id.txt_Food_Detail_Price);
        tv_description = findViewById(R.id.txt_Food_Detail_Des);
        food_image = findViewById(R.id.img_Food_Detail_img);
        img_feedback = findViewById(R.id.image_feelback_other);



        floatingActionButton_Cart = findViewById(R.id.fab_Cart);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Food");
        ratingTb1 = firebaseDatabase.getReference("Rating");
        img_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showRatingDialog();
            }
        });
        ratingBar = findViewById(R.id.ratingFood);
        if (getIntent()!= null)
            foodId = getIntent().getStringExtra("FoodId");

        if (!foodId.isEmpty() && foodId!= null ){
            if (Common.isConnectedtoInternet(getBaseContext())) {
                loadFoodDetail(foodId);
//                getRatingFood(foodId);
            }
            else {
                Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void loadFoodDetail(String foodId) {
        databaseReference.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .placeholder(R.drawable.imgerror)
                        .error(R.drawable.imgerror)
                        .into(food_image);
                tv_price.setText(currentFood.getPrice());
                tv_name.setText(currentFood.getName());
                tv_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
