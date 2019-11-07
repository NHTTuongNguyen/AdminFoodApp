package com.example.admin_project.FoodList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin_project.Common.Common;
import com.example.admin_project.FoodDetail.FoodDetail;
import com.example.admin_project.Home.Home;
import com.example.admin_project.Interface.ItemClickListener;
import com.example.admin_project.Model.Category;
import com.example.admin_project.Model.Food;
import com.example.admin_project.R;
import com.example.admin_project.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class FoodList extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference_Category;
    String categoryId = "";
    String saleId = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private final  int PICK_IMAGE_REQUEST = 71;
    Uri saveUri;
    Food newFood;
    StorageReference storageReference;
    FirebaseStorage storage;
    TextView txtNameMenu;
    EditText edtName,edtDescription,edtPrice,edtDiscount;
    Button btnSelect,btnUpload;
    TextView tv_category;
    ImageView img_category;




    CoordinatorLayout rootlayout;

    FloatingActionButton floatingActionButton;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        rootlayout = findViewById(R.id.rootlayout);
        tv_category = findViewById(R.id.tv_category);
        img_category = findViewById(R.id.img_category);
        swipeRefreshLayout = findViewById(R.id.swipe_layout_list);
        floatingActionButton = findViewById(R.id.fab_addfoodlist);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaLogListFood();
            }
        });



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Food");///Cars
        databaseReference_Category = firebaseDatabase.getReference("Category");///Category

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView = findViewById(R.id.recycler_listfood);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        swipeRefreshLayout.setColorSchemeResources(R.color.black,
                android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark,
                android.R.color.background_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent() != null) {
                    categoryId = getIntent().getStringExtra("CategoryId");
                }
                if (!categoryId.isEmpty() && categoryId!= null){
                    if (Common.isConnectedtoInternet(getBaseContext())) {
                        loadMenuListFood(categoryId);
                        loafMenuCategory(categoryId);

                    }
                    else {
                        Toast.makeText(getBaseContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (getIntent() != null) {
                    categoryId = getIntent().getStringExtra("CategoryId");
                }
                if (!categoryId.isEmpty() && categoryId!= null){
                    if (Common.isConnectedtoInternet(getBaseContext())) {
                        loadMenuListFood(categoryId);
                        loafMenuCategory(categoryId);

                    }
                    else {
                        Toast.makeText(getBaseContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }

    private void showDiaLogListFood() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this)
                ;
//        alertDialog.setTitle("Add new Food");
//        alertDialog.setMessage("Please fill fun imformation");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.dialog_list_food,null);
        edtName = add_menu_layout.findViewById(R.id.edtName_list);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription_list);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice_list);
        edtDiscount = add_menu_layout.findViewById(R.id.edtDiscount_list);
        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_action_add);
        ///set Button
        btnSelect = add_menu_layout.findViewById(R.id.select_listfood);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();/// Let user selet image from Gallery and save Uri of this image
            }
        });
        btnUpload = add_menu_layout.findViewById(R.id.upload_listfood);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

            }
        });


        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ///here just create new category
                if (newFood != null){
                    databaseReference.push().setValue(newFood);
                    Snackbar.make(rootlayout,"New caterory "+newFood.getName()+" was added", Snackbar.LENGTH_SHORT)
                            .show();


                }


            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.show();

    }

    private void uploadImage() {
        if (saveUri != null)
        {
            final ProgressDialog mDialog  = new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ///set value for newCategory if image upload and we can get download link
                                    newFood = new Food();
                                    newFood.setName(edtName.getText().toString());
                                    newFood.setDescription(edtDescription.getText().toString());
                                    newFood.setDiscount(edtDiscount.getText().toString());
                                    newFood.setPrice(edtPrice.getText().toString());
                                    newFood.setMenuId(categoryId);
                                    newFood.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded"+progress+"%");
                        }
                    });
        }


    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.UPDATE))
        {
            showDialogUpdate(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else  if (item.getTitle().equals(Common.DELETE))
        {
            deleteCategory(adapter.getRef(item.getOrder()).getKey());
            Toast.makeText(this, "Item Delete", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    private void showDialogUpdate(final String key, final Food item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
//        alertDialog.setTitle("Edit Food");
//        alertDialog.setMessage("Please fill fun imformation");
        final LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.dialog_list_food,null);
        edtName = add_menu_layout.findViewById(R.id.edtName_list);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription_list);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice_list);
        edtDiscount = add_menu_layout.findViewById(R.id.edtDiscount_list);



        ////set daufault value  for view
        edtName.setText(item.getName());
        edtDescription.setText(item.getDescription());
        edtPrice.setText(item.getPrice());
        edtDiscount.setText(item.getDiscount());

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_action_add);
        ///set Button
        btnSelect = add_menu_layout.findViewById(R.id.select_listfood);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();/// Let user selet image from Gallery and save Uri of this image
            }
        });
        btnUpload = add_menu_layout.findViewById(R.id.upload_listfood);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);

            }
        });


        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();



                ///here just create new category
                    item.setName(edtName.getText().toString());
                    item.setPrice(edtPrice.getText().toString());
                    item.setDiscount(edtDiscount.getText().toString());
                    item.setDescription(edtDescription.getText().toString());

//                String edtNameEmpty = edtName.getText().toString().trim();
//                String edtDescriptionEmpty = edtDescription.getText().toString().trim();
//                String edtPriceEmpty = edtPrice.getText().toString().trim();
//                String  edtDiscountEmpty = edtDiscount.getText().toString().trim();
//
//                if (TextUtils.isEmpty(edtNameEmpty) ||TextUtils.isEmpty(edtDescriptionEmpty)||TextUtils.isEmpty(edtPriceEmpty)||TextUtils.isEmpty(edtDiscountEmpty)) {
//                    Toast.makeText(getApplicationContext(), "Please Enter Your Food Name!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                    databaseReference.child(key).setValue(item);
                    Snackbar.make(rootlayout," Food "+item.getName()+" was edited", Snackbar.LENGTH_SHORT)
                            .show();




            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.show();
    }

    private void deleteCategory(String key) {
        databaseReference.child(key).removeValue();
    }



    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }
    private void changeImage(final Food item) {
        if (saveUri != null)
        {
            final ProgressDialog mDialog  = new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ///set value for newCategory if image upload and we can get download link
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded"+progress+"%");
                        }
                    });
        }

    }

    private void loafMenuCategory(final String categoryId) {
        databaseReference_Category.child(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Category category = dataSnapshot.getValue(Category.class);
                tv_category.setText(category.getName());
                Picasso.with(getBaseContext()).load(category.getImage())
                        .into(img_category);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMenuListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.item_food,
                FoodViewHolder.class,
                databaseReference.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage())
                        .into(foodViewHolder.food_image);
                foodViewHolder.food_price.setText(String.format("$ %s",food.getPrice()));

                final Food clickFood = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                       Intent intent = new Intent(FoodList.this, FoodDetail.class);
                        intent.putExtra("FoodId",adapter.getRef(position).getKey());
                       startActivity(intent);
                    }
                });


            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Selected!");
        }

    }
}
