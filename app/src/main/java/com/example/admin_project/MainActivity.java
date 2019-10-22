package com.example.admin_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.admin_project.Common.Common;
import com.example.admin_project.Home.Home;
import com.example.admin_project.Model.User;
import com.example.admin_project.Screen.Login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello W");
        Paper.init(this);
        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
//        if (user != null && pwd != null  )
//        {
//            if (!user.isEmpty()&& !pwd.isEmpty())
//                login(user,pwd);
//        }
    }

//    private void login(String user, String pwd) {
//        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        final DatabaseReference databaseReference = firebaseDatabase.getReference("User");
//        if (Common.isConnectedtoInternet(getBaseContext())) {
//
//            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
//            mDialog.setMessage("Please Waiting...");
//            mDialog.show();
//            final String localPhone = phone;
//            final String localPassword = password;
//
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.child(localPhone).exists()) {
//                        mDialog.dismiss();
//                        User user = dataSnapshot.child(localPhone).getValue(User.class);
//                        user.setPhone(localPhone);
//                        // if isstaff == true
//                        if (Boolean.parseBoolean(user.getIsStaff())) {
//                            if (user.getPassword().equals(localPassword)) {
//                                ///login ok
//                                Intent i = new Intent(MainActivity.this, Home.class);
//                                Common.currentUser = user;
//                                startActivity(i);
//                                finish();
//                            } else {
//                                Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(MainActivity.this, "Please login with staff account", Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else {
//                        mDialog.dismiss();
//                        Toast.makeText(MainActivity.this, "User is not exist in database ", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//        else {
//            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
//        }
//    }
}
