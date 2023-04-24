package com.example.tuderm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FixPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    View view_01,view_02,view_03,view_04;
    Button btn_01,btn_02,btn_03,btn_04,btn_05,btn_06,btn_07,btn_08,btn_09,btn_00;
    ImageButton btn_clear;
    ArrayList<String> numbers_list =  new ArrayList<>();
    String passcode = "";
    String num_01,num_02,num_03,num_04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fix_password);
        initializeComponents();
    }

    @SuppressLint("WrongViewCast")
    private void initializeComponents() {
        view_01 = findViewById(R.id.view_01);
        view_02 = findViewById(R.id.view_02);
        view_03 = findViewById(R.id.view_03);
        view_04 = findViewById(R.id.view_04);

        btn_01 = findViewById(R.id.btn_01);
        btn_02 = findViewById(R.id.btn_02);
        btn_03 = findViewById(R.id.btn_03);
        btn_04 = findViewById(R.id.btn_04);
        btn_05 = findViewById(R.id.btn_05);
        btn_06 = findViewById(R.id.btn_06);
        btn_07 = findViewById(R.id.btn_07);
        btn_08 = findViewById(R.id.btn_08);
        btn_09 = findViewById(R.id.btn_09);
        btn_00 = findViewById(R.id.btn_00);
        btn_clear = findViewById(R.id.btn_clear);

        btn_01.setOnClickListener(this);
        btn_02.setOnClickListener(this);
        btn_03.setOnClickListener(this);
        btn_04.setOnClickListener(this);
        btn_05.setOnClickListener(this);
        btn_06.setOnClickListener(this);
        btn_07.setOnClickListener(this);
        btn_08.setOnClickListener(this);
        btn_09.setOnClickListener(this);
        btn_00.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_01:
                numbers_list.add("1");
                passnumber(numbers_list);
                break;
            case R.id.btn_02:
                numbers_list.add("2");
                passnumber(numbers_list);
                break;
            case R.id.btn_03:
                numbers_list.add("3");
                passnumber(numbers_list);
                break;
            case R.id.btn_04:
                numbers_list.add("4");
                passnumber(numbers_list);
                break;
            case R.id.btn_05:
                numbers_list.add("5");
                passnumber(numbers_list);
                break;
            case R.id.btn_06:
                numbers_list.add("6");
                passnumber(numbers_list);
                break;
            case R.id.btn_07:
                numbers_list.add("7");
                passnumber(numbers_list);
                break;
            case R.id.btn_08:
                numbers_list.add("8");
                passnumber(numbers_list);
                break;
            case R.id.btn_09:
                numbers_list.add("9");
                passnumber(numbers_list);
                break;
            case R.id.btn_00:
                numbers_list.add("0");
                passnumber(numbers_list);
                break;
            case R.id.btn_clear:
                if(!numbers_list.isEmpty()){
                    numbers_list.remove(numbers_list.size() - 1);
                    passnumber(numbers_list);
                }
                break;
        }
    }

    private void passnumber(ArrayList<String> numbers_list) {
        if(numbers_list.size() == 0 ){
            view_01.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_02.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_03.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_04.setBackgroundResource(R.drawable.bg_view_grey_oval);

        }else{
            switch (numbers_list.size()){
                case 1:
                    num_01 = numbers_list.get(0);
                    view_01.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    view_02.setBackgroundResource(R.drawable.bg_view_grey_oval);
                    view_03.setBackgroundResource(R.drawable.bg_view_grey_oval);
                    view_04.setBackgroundResource(R.drawable.bg_view_grey_oval);
                    break;
                case 2:
                    num_02 = numbers_list.get(1);
                    view_02.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    view_03.setBackgroundResource(R.drawable.bg_view_grey_oval);
                    view_04.setBackgroundResource(R.drawable.bg_view_grey_oval);
                    break;
                case 3:
                    num_03 = numbers_list.get(2);
                    view_03.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    view_04.setBackgroundResource(R.drawable.bg_view_grey_oval);
                    break;
                case 4:
                    num_04 = numbers_list.get(3);
                    view_04.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    savedata();
                    break;
            }
        }
    }

    private void savedata() {
        passcode  = num_01 + num_02 + num_03 + num_04;
        DBHelper dbh = new DBHelper(this);
        int index = dbh.getdoctorfid();
        dbh.updateDoctorPassword(index,passcode);
        Toast.makeText(this,"แก้ไขข้อมูลเรียบร้อย",Toast.LENGTH_SHORT).show();
        Intent L = new Intent(FixPasswordActivity.this, SettingUserActivity.class);
        startActivity(L);
    }
}