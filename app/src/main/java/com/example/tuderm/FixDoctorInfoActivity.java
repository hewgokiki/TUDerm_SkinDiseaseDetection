package com.example.tuderm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;

public class FixDoctorInfoActivity extends AppCompatActivity {

    TextView titletext;
    TextInputLayout firstnametext,lastnametext;
    Button adddoctordata;
    ImageView pic,back;
    FloatingActionButton fab;
    MenuBuilder menuBuilder;
    byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fix_doctor_info);
        settitle();
        setsourceofimage();
        adddoctorbtn();
        backtoprevious();
    }

    private void backtoprevious() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @SuppressLint("RestrictedApi")
    private void setsourceofimage() {
        menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.popupimagemenu, menuBuilder);
        pic = findViewById(R.id.addpicture);
        DBHelper dbh = new DBHelper(this);
        pic.setImageBitmap(dbh.getdoctorpic());
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuPopupHelper optionmenu = new MenuPopupHelper(FixDoctorInfoActivity.this,menuBuilder,view);
                optionmenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu_opencamera:
                                ImagePicker.with(FixDoctorInfoActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .cameraOnly()
                                        .start();
                                return true;

                            case R.id.menu_opengallery:
                                ImagePicker.with(FixDoctorInfoActivity.this)
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
                MenuPopupHelper optionmenu = new MenuPopupHelper(FixDoctorInfoActivity.this,menuBuilder,view);
                optionmenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu_opencamera:
                                ImagePicker.with(FixDoctorInfoActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .cameraOnly()
                                        .start();
                                return true;

                            case R.id.menu_opengallery:
                                ImagePicker.with(FixDoctorInfoActivity.this)
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
        adddoctordata = findViewById(R.id.adddoctorbtn);
        firstnametext = findViewById(R.id.doc_firstname);
        lastnametext = findViewById(R.id.doc_lastname);
        titletext = findViewById(R.id.doc_filled_exposed);
        adddoctordata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senddata();
            }
        });
    }

    private void senddata() {
        DBHelper dbh = new DBHelper(this);
        Cursor cursor = dbh.getdoctordata();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int index = cursor.getInt(cursor.getColumnIndex("doc_id"));
                String title = cursor.getString(cursor.getColumnIndex("doc_title"));
                String name = cursor.getString(cursor.getColumnIndex("doc_firstname"));
                String lastname = cursor.getString(cursor.getColumnIndex("doc_lastname"));
                String password = cursor.getString(cursor.getColumnIndex("doc_password"));
                String Stitle = null,Sname = null,Slastname = null;
                if(TextUtils.isEmpty(titletext.getText().toString())){
                    Stitle = title;
                }else{
                    Stitle = titletext.getText().toString();
                }
                if(TextUtils.isEmpty(firstnametext.getEditText().getText().toString())){
                    Sname = name;
                }else{
                    Sname = firstnametext.getEditText().getText().toString();
                }
                if(TextUtils.isEmpty(lastnametext.getEditText().getText().toString())){
                    Slastname = lastname;
                }else{
                    Slastname = lastnametext.getEditText().getText().toString();
                }
                dbh.updateDoctor(index,bytes,Stitle,Sname,Slastname,password);
                Toast.makeText(this,"แก้ไขข้อมูลเรียบร้อย",Toast.LENGTH_SHORT).show();
                Intent L = new Intent(FixDoctorInfoActivity.this, SettingUserActivity.class);
                startActivity(L);
            } while (cursor.moveToNext());
        }

    }


}