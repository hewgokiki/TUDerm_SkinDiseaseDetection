package com.example.tuderm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EncyActivity extends AppCompatActivity implements RecycleViewInterface {
    ImageView back;
    SQLiteDatabase sqLiteDatabase;
    encyAdapter encyAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ency);
        backtoprevious();
        setRecycle();
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

    private void setRecycle() {
        ArrayList<EncyModel> EncyModelArrayList = new ArrayList<>();
        //getdata
        DBHelper dbh = new DBHelper(this);
        Cursor cursor = dbh.getDiseasedata();
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do{
                int index = cursor.getInt(0);
                byte[] image = cursor.getBlob(1);
                String text = cursor.getString(2);
                EncyModelArrayList.add(new EncyModel(index,image,text));

            }while(cursor.moveToNext());
            sqLiteDatabase = dbh.getReadableDatabase();
            encyAdapter = new encyAdapter(this,R.layout.row_encyclop, EncyModelArrayList,sqLiteDatabase,this);
            //set layout
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setAdapter(encyAdapter);
            recyclerView.setLayoutManager((new LinearLayoutManager(this,recyclerView.VERTICAL,false)));
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}