package com.example.tuderm;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FullDiseaseActivity extends AppCompatActivity {
    ImageView imageView,back;
    TextView nameTextView,textTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_full_disease);
        setEnviroment();
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

    private void setEnviroment() {
        imageView = findViewById(R.id.diseaseimage);
        nameTextView = findViewById(R.id.diseasename);
        textTextView = findViewById(R.id.diseasetext);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        DBHelper dbh = new DBHelper(this);
        Cursor cursor = dbh.getDiseaseData(position);
        if (cursor.moveToFirst()) {
            // Retrieve data from cursor
            String name = cursor.getString(cursor.getColumnIndex("Disease_name"));
            String text = cursor.getString(cursor.getColumnIndex("Disease_info"));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("Disease_image"));
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            // Assign data to views
            imageView.setImageBitmap(imageBitmap);
            nameTextView.setText(name);
            textTextView.setText(text);
        }
    }
}