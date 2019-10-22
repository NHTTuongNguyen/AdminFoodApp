package com.example.admin_project.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin_project.Common.Common;
import com.example.admin_project.Home.Home;
import com.example.admin_project.Model.User;
import com.example.admin_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    EditText edtPhone,edtPassword;
    Button btLogin;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btLogin = findViewById(R.id.btLogin);
        checkBox = findViewById(R.id.checkbox);
        ///
//        StatusBarUtil.setTransparent(Login.this);
        Paper.init(this);

        ///
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser(edtPhone.getText().toString(),edtPassword.getText().toString());
            }
        });


    }

    private void LoginUser(String phone, String password) {
        if (Common.isConnectedtoInternet(getBaseContext())) {
            if (checkBox.isChecked()){
                if (checkBox.isChecked()){
                    Paper.book().write(Common.USER_KEY,edtPhone.getText().toString());
                    Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());

                }
            }
            final ProgressDialog mDialog = new ProgressDialog(Login.this);
            mDialog.setMessage("Please Waiting...");
            mDialog.show();
            final String localPhone = phone;
            final String localPassword = password;

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(localPhone).exists()) {
                        mDialog.dismiss();
                        User user = dataSnapshot.child(localPhone).getValue(User.class);
                        user.setPhone(localPhone);
                        // if isstaff == true
                        if (Boolean.parseBoolean(user.getIsStaff())) {
                            if (user.getPassword().equals(localPassword)) {
                                ///login ok
                                Intent i = new Intent(Login.this, Home.class);
                                Common.currentUser = user;
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Please login with staff account", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        mDialog.dismiss();
                        Toast.makeText(Login.this, "User is not exist in database ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
        }

    }
}
