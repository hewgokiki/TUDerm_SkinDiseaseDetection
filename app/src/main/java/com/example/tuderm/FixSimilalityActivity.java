package com.example.tuderm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class FixSimilalityActivity extends AppCompatActivity implements RecycleViewInterface {
    ImageView back;
    Button savebtn;
    DBHelper dbh = new DBHelper(this);
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    com.example.tuderm.similalityAdapter similalityAdapter;
    TextInputLayout diseaseTextInputLayout,textTextInputLayout;
    ArrayList<similalityModel> similalityModelArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fix_similality);
        backtoprevious();
        getdata();
        displaydata();
    }

    private void displaydata() {
        //getdata
        Cursor cursor = dbh.getSimilalityData();
        while(cursor.moveToNext()){
            int index = cursor.getInt(0);
            String stext = "คำคล้าย: " +cursor.getString(1);
            String sdisease = "โรค: " +cursor.getString(3);
            similalityModelArrayList.add(new similalityModel(index,stext,sdisease));
        }
        sqLiteDatabase = dbh.getReadableDatabase();
        similalityAdapter = new similalityAdapter(this,R.layout.row_patient, similalityModelArrayList,sqLiteDatabase,this);

        //set layout
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setAdapter(similalityAdapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(this,recyclerView.VERTICAL,false)));

    }

    private void getdata() {
        savebtn = findViewById(R.id.savebtn);
        diseaseTextInputLayout = findViewById(R.id.diseasetxt);
        textTextInputLayout = findViewById(R.id.texttxt);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i =0;
                if(TextUtils.isEmpty(diseaseTextInputLayout.getEditText().getText().toString())){
                    Toast.makeText(view.getContext(), "กรุณากรอกโรค", Toast.LENGTH_SHORT).show();
                    i++;
                }
                if(TextUtils.isEmpty(textTextInputLayout.getEditText().getText().toString())){
                    Toast.makeText(view.getContext(), "กรุณากรอกคำคล้าย", Toast.LENGTH_SHORT).show();
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
        String d = diseaseTextInputLayout.getEditText().getText().toString();
        String t = textTextInputLayout.getEditText().getText().toString();
        int countrepeat = dbh.checkrepeatSimilality(t,d);
        System.out.println(countrepeat);
        if(countrepeat > 0){
            Toast.makeText(this, "ข้อมูลโรคซ้ำกัน กรุณากรอกใหม่", Toast.LENGTH_SHORT).show();
        }else{
            dbh.addSimilalityword(t,d);
            Toast.makeText(this,"บันทึกข้อมูลเรียบร้อย",Toast.LENGTH_SHORT).show();
            Intent L = new Intent(FixSimilalityActivity.this, SettingUserActivity.class);
            startActivity(L);
        }
    }

    private void backtoprevious() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(FixSimilalityActivity.this, SettingUserActivity.class);
                startActivity(L);
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}