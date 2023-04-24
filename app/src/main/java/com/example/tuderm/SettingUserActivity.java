package com.example.tuderm;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingUserActivity extends AppCompatActivity {

    Button fixdoctorinfo, fixpassword, fixsimilality, importData, exportData;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_setting_user);
        backtoprevious();
        tofixdoctorinfo();
        tofixpassword();
        tofixsimilality();
        toexportData();
        toimportData();
    }

    private void backtoprevious() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(SettingUserActivity.this, MainPageActivity.class);
                startActivity(L);
            }
        });
    }

    private void tofixsimilality() {
        fixsimilality = findViewById(R.id.fixsimilality);
        fixsimilality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(SettingUserActivity.this, FixSimilalityActivity.class);
                startActivity(L);
            }
        });
    }

    private void tofixdoctorinfo() {
        fixdoctorinfo = findViewById(R.id.fixdoctorinfo);
        fixdoctorinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(SettingUserActivity.this, FixDoctorInfoActivity.class);
                startActivity(L);
            }
        });
    }

    private void tofixpassword() {
        fixpassword = findViewById(R.id.fixpassword);
        fixpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(SettingUserActivity.this, FixPasswordActivity.class);
                startActivity(L);
            }
        });
    }

    private void toexportData() {
        exportData = findViewById(R.id.exportData);
        exportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Downloads.DISPLAY_NAME, "App.DB");
                    values.put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream");
                    values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    @SuppressLint({"NewApi", "LocalSuppress"}) Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    InputStream inputStream = new FileInputStream(
                            "/data/data/com.example.tuderm/databases/App.DB");

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }

                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();

                    Toast.makeText(view.getContext(), "ส่งออกไปที่ SDCARD/Downloads/App.DB", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toimportData() {
        importData = findViewById(R.id.importData);
        importData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open file explorer to select the database file
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Get the selected file URI
            Uri uri = data.getData();

            // Copy the database file to the app's data directory
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                File outputFile = new File(getDatabasePath("App.DB").getPath());
                OutputStream outputStream = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();

                // Notify the user that the import was successful
                Toast.makeText(getApplicationContext(), "นำเข้าข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                // Notify the user that there was an error importing the database
                Toast.makeText(getApplicationContext(), "นำเข้าข้อมูลไม่สำเร็จ", Toast.LENGTH_SHORT).show();
            }
        }
    }


}