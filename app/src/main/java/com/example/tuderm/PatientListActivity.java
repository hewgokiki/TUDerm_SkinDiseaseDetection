package com.example.tuderm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class PatientListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RecycleViewInterface {

    Spinner spn_sort;
    DBHelper dbh;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    com.example.tuderm.patientAdapter patientAdapter;
    SearchView searchView;
    ImageView back;
    ArrayList<patientModel> patientModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_patient_list);
        dbh = new DBHelper(this);
        backtoprevious();
        displaydata();
        searchpatient();
        sort();
    }

    private void backtoprevious() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(PatientListActivity.this, MainPageActivity.class);
                startActivity(L);
            }
        });
    }

    private void searchpatient() {
        searchView = findViewById(R.id.searchview);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
    }

    private void filterList(String text) {
        ArrayList<patientModel> filteredList = new ArrayList<>();
        for(patientModel p: patientModelArrayList){
            if(p.getFullname().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(p);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this,"ไม่พบข้อมูล",Toast.LENGTH_SHORT).show();
            ArrayList<patientModel> n = new ArrayList<>();
            patientAdapter.setFilteredList(n); //for null input, for invalid input
        }else{
            patientAdapter.setFilteredList(filteredList);
        }

    }

    private void displaydata() {
        //getdata
        Cursor cursor = dbh.getpatientdata();
        while(cursor.moveToNext()){
            int index = cursor.getInt(0);
            byte[] image = cursor.getBlob(1);
            String name = cursor.getString(3);
            String lastname = cursor.getString(4);
            String fullname = name+" "+lastname;
            patientModelArrayList.add(new patientModel(index,image,fullname));
        }
        sqLiteDatabase = dbh.getReadableDatabase();
        patientAdapter = new patientAdapter(this,R.layout.row_patient, patientModelArrayList,sqLiteDatabase,this);

        //set layout
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setAdapter(patientAdapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(this,recyclerView.VERTICAL,false)));

    }

    private void sort() {
        spn_sort = findViewById(R.id.spinner);
        String[] s = getResources().getStringArray(R.array.spin_sort_array);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spn_sort.setAdapter(adapter);
        spn_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                if(position==0){
                    Collections.sort(patientModelArrayList,(l1, l2)->{
                        if(l1.getIndex()>l2.getIndex()){
                            return -1;
                        }else if(l1.getIndex()<l2.getIndex()){
                            return 1;
                        }else{
                            return 0;
                        }
                    }); //sort index
                }else if(position==1){
                    Collections.sort(patientModelArrayList, new ThaiStringComparator());
                    //sort String thai and eng
                }
                patientAdapter.notifyDataSetChanged();
                //change data when change sort filter
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onItemClick(int position) {
        Intent L = new Intent(PatientListActivity.this, PatientOptionActivity.class);
        L.putExtra("pat_index", patientModelArrayList.get(position).getIndex());
        startActivity(L);
    }
}