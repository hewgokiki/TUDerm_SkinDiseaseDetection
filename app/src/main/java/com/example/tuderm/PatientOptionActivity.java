package com.example.tuderm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class PatientOptionActivity extends AppCompatActivity implements RecycleViewInterface {
    TextView name;
    Button add;
    ImageView pic,back;
    DBHelper dbh;
    int pat_index;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    DiagnosisAdapter diagnosisAdapter;
    ArrayList<diagnosisModel> diagnosisModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_patient_option);
        setenviroment();
        getDataFromIntent();
        setRecycle();
        backtoprevious();
        adddiagnosis();
    }

    private void sortItemsByTimestamp(ArrayList<diagnosisModel> d) {
        Collections.sort(d,(l1,l2)->{
            if(l1.getIndex()>l2.getIndex()){
                return -1;
            }else if(l1.getIndex()<l2.getIndex()){
                return 1;
            }else{
                return 0;
            }
        });
    }
    private void setRecycle() {

        Cursor cursor = dbh.getdiagnosisbypatient(pat_index);

        while (cursor.moveToNext()) {
            int index = cursor.getInt(0);
            String dia = cursor.getString(cursor.getColumnIndex("dia_diagnosisbydoctor"));
            String date = cursor.getString(cursor.getColumnIndex("dia_date"));
            String time = cursor.getString(cursor.getColumnIndex("dia_time"));
            String fulldatetime = date +" "+time;
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("img_file"));
            Bitmap image = null;
            if (imageBytes != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4; // reduce the size by a factor of 4
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
            }

            diagnosisModelArrayList.add(new diagnosisModel(index,imageBytes,dia,fulldatetime));
        }
        cursor.close();

        sqLiteDatabase = dbh.getReadableDatabase();
        diagnosisAdapter = new DiagnosisAdapter(this,R.layout.row_diagnosis, diagnosisModelArrayList,sqLiteDatabase,this);

        //sort
        sortItemsByTimestamp(diagnosisModelArrayList);

        //set layout
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(diagnosisAdapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(this,recyclerView.VERTICAL,false)));

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        pat_index = intent.getIntExtra("pat_index",0);
        dbh = new DBHelper(this);
        name.setText(dbh.getpatientname(pat_index));
        pic.setImageBitmap(dbh.getpatientpic(pat_index));
    }

    private void setenviroment() {
        name = findViewById(R.id.patientfullname);
        pic = findViewById(R.id.patientpic);
        back = findViewById(R.id.back);
        add = findViewById(R.id.adddiagnosis);
    }

    private void backtoprevious() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent L = new Intent(PatientOptionActivity.this, PatientListActivity.class);
                startActivity(L);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void adddiagnosis() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(PatientOptionActivity.this, DoctorDiagnosisActivity.class);
                L.putExtra("pat_index",pat_index);
                startActivity(L);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent L = new Intent(PatientOptionActivity.this, DiagnosisSummaryActivity.class);
        L.putExtra("dia_index", diagnosisModelArrayList.get(position).getIndex());
        L.putExtra("pat_index",pat_index);
        System.out.println(diagnosisModelArrayList.get(position).getIndex());
        startActivity(L);
    }
}