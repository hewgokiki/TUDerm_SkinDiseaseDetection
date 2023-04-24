package com.example.tuderm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class MainPageActivity extends AppCompatActivity {

    LinearLayout add,list,ai,ency,stat,setting;
    DBHelper dbh;
    MenuBuilder menuBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_page);
        setimageandname();
        addpatient();
        patientlist();
        aidiagnosis();
        encyclopedia();
        stat();
        setting();
    }


    @SuppressLint("RestrictedApi")
    private void aidiagnosis() {
        menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.popupimagemenu, menuBuilder);
        ai = findViewById(R.id.quickailayout);
        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuPopupHelper optionmenu = new MenuPopupHelper(MainPageActivity.this,menuBuilder,view);
                optionmenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu_opencamera:
                                ImagePicker.with(MainPageActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .cameraOnly()
                                        .start();
                                return true;

                            case R.id.menu_opengallery:
                                ImagePicker.with(MainPageActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .galleryOnly()
                                        .start();
                                return true;
                        }
                        return false;
                    }
                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {
                    }
                });
                optionmenu.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getExtras()  != null){
            Uri uri = data.getData();
            Intent L = new Intent(MainPageActivity.this, QuickAIScanActivity.class);
            L.putExtra("imageUri", uri.toString());
            startActivity(L);
        }
    }

    private void encyclopedia() {
        ency = findViewById(R.id.encyclopedia);
        ency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(MainPageActivity.this, EncyActivity.class);
                startActivity(L);
            }
        });
    }


    private void stat() {
        stat = findViewById(R.id.stat);
        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(MainPageActivity.this, StatActivity.class);
                startActivity(L);
            }
        });
    }

    private void setting() {
        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(MainPageActivity.this, SettingUserActivity.class);
                startActivity(L);
            }
        });
    }


    private void setimageandname() {
        dbh = new DBHelper(this);
        ImageView img = findViewById(R.id.doctorpic);
        img.setImageBitmap(dbh.getdoctorpic());
        TextView docname = findViewById(R.id.doctorfullname);
        docname.setText(dbh.getdoctorname());
    }


    private void addpatient() {
        add = findViewById(R.id.addpatientlayout);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(MainPageActivity.this, AddPatientActivity.class);
                startActivity(L);
            }
        });
    }

    private void patientlist() {
        list = findViewById(R.id.patientlistlayout);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(MainPageActivity.this, PatientListActivity.class);
                startActivity(L);
            }
        });
    }



}