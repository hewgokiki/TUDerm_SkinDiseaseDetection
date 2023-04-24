package com.example.tuderm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;

public class DoctorRegisterActivity extends AppCompatActivity {
    DBHelper dbh = new DBHelper(this);
    TextView titletext;
    TextInputLayout firstnametext,lastnametext;
    Button adddoctordata;
    ImageView pic;
    FloatingActionButton fab;
    MenuBuilder menuBuilder;
    byte[] bytes;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_doctor_register);

        titletext =  findViewById(R.id.doc_filled_exposed);
        firstnametext =  findViewById(R.id.doc_firstname);
        lastnametext =  findViewById(R.id.doc_lastname);
        setsourceofimage();
        setdefaultimagefordatabase();
        settitle();
        adddoctorbtn();
    }

    @SuppressLint("RestrictedApi")
    private void setsourceofimage() {
        menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.popupimagemenu, menuBuilder);
        pic = findViewById(R.id.addpicture);
        setdefaultimagefordatabase();
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuPopupHelper optionmenu = new MenuPopupHelper(DoctorRegisterActivity.this,menuBuilder,view);
                optionmenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu_opencamera:
                                ImagePicker.with(DoctorRegisterActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .cameraOnly()
                                        .start();
                                return true;

                            case R.id.menu_opengallery:
                                ImagePicker.with(DoctorRegisterActivity.this)
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

        fab =  findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuPopupHelper optionmenu = new MenuPopupHelper(DoctorRegisterActivity.this,menuBuilder,view);
                optionmenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu_opencamera:
                                ImagePicker.with(DoctorRegisterActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .cameraOnly()
                                        .start();
                                return true;

                            case R.id.menu_opengallery:
                                ImagePicker.with(DoctorRegisterActivity.this)
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

    private void setdefaultimagefordatabase() {
        pic.setImageResource(R.drawable.defaultprofileimage);
        Bitmap bitmap = ((BitmapDrawable) pic.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        bytes = stream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getExtras()  != null){
            Uri uri = data.getData();
            pic.setImageURI(uri);
            Bitmap bitmap = ((BitmapDrawable) pic.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
            bytes = stream.toByteArray();
        }
    }

    private void settitle() {
        String[] sort = new String[]{"นายแพทย์","แพทย์หญิง","ไม่ระบุ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.dropdown_item,sort);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.doc_filled_exposed);
        autoCompleteTextView.setAdapter(adapter);
    }


    private void adddoctorbtn() {
        dbh = new DBHelper(this);
        adddoctordata = findViewById(R.id.adddoctorbtn);
        adddoctordata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(firstnametext.getEditText().getText().toString())){
                    Toast.makeText(view.getContext(), "กรุณากรอกชื่อ", Toast.LENGTH_SHORT).show();
                    i++;
                }
                if(TextUtils.isEmpty(lastnametext.getEditText().getText().toString())){
                    Toast.makeText(view.getContext(), "กรุณากรอกนามสกุล", Toast.LENGTH_SHORT).show();
                    i++;
                }
                if(i==0){
                    senddata();
                }
                i=0;
            }
        });
    }

    private void senddata() {
        //set string
        String Stitle = titletext.getText().toString();
        String Sname = firstnametext.getEditText().getText().toString();
        String Slastname = lastnametext.getEditText().getText().toString();
        Toast.makeText(this,"บันทึกข้อมูลเรียบร้อย",Toast.LENGTH_SHORT).show();
        //send string to register activity
        Intent L = new Intent(DoctorRegisterActivity.this, RegisterActivity.class);
        L.putExtra("byteimage",bytes);
        L.putExtra("title",Stitle);
        L.putExtra("name",Sname);
        L.putExtra("lastname",Slastname);
        startActivity(L);
    }
}