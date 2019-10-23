package com.example.admin_project.Home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.admin_project.Common.Common;
import com.example.admin_project.FoodList.FoodList;
import com.example.admin_project.Interface.ItemClickListener;
import com.example.admin_project.ListCustomer.Listcustomer;
import com.example.admin_project.Model.Category;
import com.example.admin_project.Model.Sale;
import com.example.admin_project.OrderStatus.OrderDetail;
import com.example.admin_project.OrderStatus.OrderStatus;
import com.example.admin_project.R;
import com.example.admin_project.ShowComments.ShowComments;
import com.example.admin_project.ViewHolder.MenuViewHolder;
import com.example.admin_project.ViewHolder.SaleViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtFullName,txtFullEmail;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference_Sale;
    FirebaseRecyclerAdapter<Category, MenuViewHolder>adapter;
    FirebaseRecyclerAdapter<Sale, SaleViewHolder>adapter_Sale;


    RecyclerView recyclerView_menu,recyclerView_Sale;
    RecyclerView.LayoutManager layoutManager;

    FirebaseStorage storage;
    StorageReference storageReference;
    Category newcategory;
    Uri saveUri;
    private final  int PICK_IMAGE_REQUEST = 71;
    EditText edtName;
    Button btnUpload,btnSelect;
    ImageView imageView_select;




    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StatusBarUtil.setTransparent(Home.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Admin");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                showDiaLog();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        ///Set Name for user
        View headerView = navigationView.getHeaderView(0);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Category");
        databaseReference_Sale = firebaseDatabase.getReference("Sale");


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());
        txtFullEmail = headerView.findViewById(R.id.txtFulEmail);
        txtFullEmail.setText(Common.currentUser.getEmail());

        recyclerView_menu = findViewById(R.id.recyclerview_Menu);
        recyclerView_menu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView_menu.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

//        recyclerView_Sale = findViewById(R.id.recycler_Sale);
//        recyclerView_Sale.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
//        recyclerView_Sale.setLayoutManager(layoutManager);



        loadMenu();
//        loadSale();

    }

//    private void loadSale() {
//        adapter_Sale = new FirebaseRecyclerAdapter<Sale, SaleViewHolder>
//                (Sale.class,R.layout.item_menu_sale,SaleViewHolder.class,databaseReference_Sale) {
//            @Override
//            protected void populateViewHolder(SaleViewHolder saleViewHolder, Sale sale, int i) {
//                saleViewHolder.txtNameSale.setText(sale.getName());
//                Picasso.with(getBaseContext())
//                        .load(sale.getImage())
//                        .into(saleViewHolder.img_sale);
//            }
//        };
//        recyclerView_Sale.setAdapter(adapter_Sale);
//    }


    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>
                (Category.class,R.layout.item_menu_food,MenuViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {
                menuViewHolder.tv_menu.setText(category.getName());
                Picasso.with(getBaseContext())
                        .load(category.getImage())
                        .placeholder(R.drawable.imgerror)
                        .error(R.drawable.error)
                        .into(menuViewHolder.img_meunu);
                final Category clickItem = category;
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent i = new Intent(Home.this, FoodList.class);
                        Toast.makeText(Home.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                        i.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(i);

                    }
                });
            }
        };
        adapter.notifyDataSetChanged(); //// Refresh data if have changed
        recyclerView_menu.setAdapter(adapter);

    }

    private void showDiaLog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.dialog_menu_food,null);
        edtName = add_menu_layout.findViewById(R.id.edtName);

        btnSelect = add_menu_layout.findViewById(R.id.select);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();/// Let user selet image from Gallery and save Uri of this image
            }
        });
        btnUpload = add_menu_layout.findViewById(R.id.upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

            }
        });
        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_action_cart);

        ///set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String  edtNameEmpty = edtName.getText().toString();

                if (TextUtils.isEmpty(edtNameEmpty)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Your Category!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ///here just create new category
                if (newcategory != null){
                    databaseReference.push().setValue(newcategory);

//                    Snackbar.make(drawer,"New category "+newcategory.getName()+"was added",Snackbar.LENGTH_SHORT)
//                            .show();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

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
                            Toast.makeText(Home.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ///set value for newCategory if image upload and we can get download link
                                    newcategory = new Category(edtName.getText().toString(),uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Image Selected!");
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
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


    private void showDialogUpdate(final String key, final Category item) {
        ////Copy code Showdialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("Please fill fun imformation");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.dialog_menu_food,null);
        edtName = add_menu_layout.findViewById(R.id.edtName);
        ////set default name
        edtName.setText(item.getName());
        btnSelect = add_menu_layout.findViewById(R.id.select);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();/// Let user selet image from Gallery and save Uri of this image
            }
        });
        btnUpload = add_menu_layout.findViewById(R.id.upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);

            }
        });
        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_action_cart);

        ///set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ///here just create new category
                item.setName(edtName.getText().toString());
                databaseReference.child(key).setValue(item);

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



    private void changeImage(final Category item) {
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
                            Toast.makeText(Home.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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


    private void deleteCategory(String key) {
        databaseReference.child(key).removeValue();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.order) {
            Intent order = new Intent(Home.this, OrderStatus.class);
            startActivity(order);

        } else if (id == R.id.show_ListCustomer) {
            Intent listcustomer = new Intent(Home.this, Listcustomer.class);
            startActivity(listcustomer);
        } else if (id == R.id.show_Comments) {
            Intent comments = new Intent(Home.this, ShowComments.class);
            startActivity(comments);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
