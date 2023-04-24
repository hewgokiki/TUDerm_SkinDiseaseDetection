package com.example.tuderm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewImageActivity extends AppCompatActivity {
    ImageView examimage,back;
    TextView aitext;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_view_image);
        setenviroment();
        getDataFromIntent();
        backtoprevious();
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);
        DBHelper dbh = new DBHelper(this);
        Cursor cursor = dbh.getSelectTempimagedata(position);
        cursor.moveToFirst();
        byte[] image = cursor.getBlob(2);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        examimage.setImageBitmap(bitmap);

        if(cursor.getString(3)!=null){
            String text = "ผลการวิเคราะห์ด้วยAI : \n"
                    +cursor.getString(3);
            aitext.setText(text);
        }
    }

    private void setenviroment() {
        back = findViewById(R.id.back);
        examimage = findViewById(R.id.examimage);
        aitext = findViewById(R.id.aitext);
    }

    private void backtoprevious() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}