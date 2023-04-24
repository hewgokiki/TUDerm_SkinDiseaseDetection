package com.example.tuderm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class AIDiagnosisActivity extends AppCompatActivity implements RecycleViewInterface {

    ImageView back;
    Button nextbtn;
    DBHelper dbh;
    SQLiteDatabase sqLiteDatabase;
    imageAdapter imageAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_aidiagnosis);
        setenviroment();
        backtoprevious();
        setRecycle();
    }

    private void setRecycle() {
        ArrayList<imageModel> imageModelArrayList = new ArrayList<>();
        //getdata
        //dbh.rearrange();
        Cursor cursor = dbh.getTempimagedata();
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do{
                int index = cursor.getInt(0);
                byte[] image = cursor.getBlob(2);
                String text = cursor.getString(3);
                System.out.println("text in ai dia ="+ text);
                imageModelArrayList.add(new imageModel(index,image,text));
            }while(cursor.moveToNext());
            sqLiteDatabase = dbh.getReadableDatabase();
            imageAdapter = new imageAdapter(this,R.layout.row_image, imageModelArrayList,sqLiteDatabase,this);
            //set layout
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setAdapter(imageAdapter);
            recyclerView.setLayoutManager((new LinearLayoutManager(this,recyclerView.VERTICAL,false)));
        }
    }

    private void setenviroment() {
        back = findViewById(R.id.back);
        nextbtn  = findViewById(R.id.nextbtn);
        dbh = new DBHelper(this);
    }

    private void backtoprevious() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}