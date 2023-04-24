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
import android.widget.TextView;

import java.util.ArrayList;

public class DiagnosisSummaryActivity extends AppCompatActivity implements RecycleViewInterface {
    String Sdiagnosis,Ssymptom,Streatment,cDate,cTime,selectbodypart;
    Button okbtn,diagnosis,symptom,treatmentandmedicine,position;
    ImageView back;
    int dia_index,pat_index;
    RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    imageAdapter imageAdapter;
    TextView setname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_diagnosis_summary);
        getdata();
        okclick();
        setRecycle();
        backtoprevious();
    }

    private void backtoprevious() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(DiagnosisSummaryActivity.this, PatientOptionActivity.class);
                L.putExtra("pat_index",pat_index);
                startActivity(L);
            }
        });
    }

    private void okclick() {
        okbtn = findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent L = new Intent(DiagnosisSummaryActivity.this, PatientOptionActivity.class);
                L.putExtra("pat_index",pat_index);
                startActivity(L);
            }
        });
    }

    private void setRecycle() {
        ArrayList<imageModel> imageModelArrayList = new ArrayList<>();
        DBHelper dbh = new DBHelper(this);
        //getdata
        Cursor cursor = dbh.getallimagedata(pat_index,dia_index);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do{
                int index = cursor.getInt(0);
                byte[] image = cursor.getBlob(1);
                String text = cursor.getString(2);
                imageModelArrayList.add(new imageModel(index,image, text));
            }while(cursor.moveToNext());
            sqLiteDatabase = dbh.getReadableDatabase();
            imageAdapter = new imageAdapter(this,R.layout.row_image, imageModelArrayList,sqLiteDatabase,this);
            //set layout
            recyclerView = findViewById(R.id.recyclerviewhorizon);
            recyclerView.setAdapter(imageAdapter);
            recyclerView.setLayoutManager((new LinearLayoutManager(this,recyclerView.HORIZONTAL,false)));
        }
    }

    private void getdata() {
        diagnosis = findViewById(R.id.diagnosis);
        symptom = findViewById(R.id.symptom);
        treatmentandmedicine = findViewById(R.id.treatmentandmedicine);
        position = findViewById(R.id.position);

        Intent intent = getIntent();
        pat_index = intent.getIntExtra("pat_index",0);
        dia_index = intent.getIntExtra("dia_index",0);
        DBHelper dbh = new DBHelper(this);
        if(dia_index == 0){
            dia_index = dbh.getdiagnosisfid();
            Sdiagnosis = intent.getStringExtra("diagnosis");
            diagnosis.setText(Sdiagnosis);
            Ssymptom = intent.getStringExtra("symptom");
            symptom.setText(Ssymptom);
            Streatment = intent.getStringExtra("treatmentandmedicine");
            treatmentandmedicine.setText(Streatment);
            selectbodypart = intent.getStringExtra("selectbodypart");
            position.setText(selectbodypart);
            cDate = intent.getStringExtra("currentDate");
            cTime = intent.getStringExtra("currentTime");
        }else{

            String d = dbh.getdiagnosisbydiaID(pat_index,dia_index);
            String s = dbh.getsymptombydiaID(pat_index,dia_index);
            String t = dbh.gettreatmentandmedicinebydiaID(pat_index,dia_index);
            String p = dbh.getpositionbydiaID(pat_index,dia_index);

            diagnosis.setText(d);
            symptom.setText(s);
            treatmentandmedicine.setText(t);
            position.setText(p);

            if(t == null || t.isEmpty()){
                treatmentandmedicine.setText("-");
            }
            if(p == null || p.isEmpty()){
                position.setText("-");
            }
        }
        setname = findViewById(R.id.setname);
        setname.setText(dbh.getpatientname(pat_index));
    }

    @Override
    public void onItemClick(int position) {

    }
}