package com.example.tuderm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BodyPartActivity extends AppCompatActivity implements View.OnClickListener {

    Button okbutton,
            head_btn,chest_btn,belly_btn,waist_btn,
            rightupperarm_btn,rightarm_btn,righthand_btn,
            leftupperarm_btn,leftarm_btn,lefthand_btn,
            leftthigh_btn,leftcalf_btn,leftfoot_btn,
            rightthigh_btn,rightcalf_btn,rightfoot_btn;

    TextView selecttxt;
    ImageView back;
    String Sdiagnosis,Ssymptom,Streatment,Smedicine,cDate,cTime,selectBodyPart;
    int pat_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_body_part);
        getdata();
        setenviroment();
        backtoprevious();
        clickbody();
    }

    private void clickbody() {
        head_btn = findViewById(R.id.head_btn);
        chest_btn = findViewById(R.id.chest_btn);
        belly_btn = findViewById(R.id.belly_btn);
        waist_btn = findViewById(R.id.waist_btn);

        leftupperarm_btn = findViewById(R.id.leftupperarm_btn);
        leftarm_btn = findViewById(R.id.leftarm_btn);
        lefthand_btn = findViewById(R.id.lefthand_btn);
        rightupperarm_btn = findViewById(R.id.rightupperarm_btn);
        rightarm_btn = findViewById(R.id.rightarm_btn);
        righthand_btn = findViewById(R.id.righthand_btn);

        leftthigh_btn = findViewById(R.id.leftthigh_btn);
        leftcalf_btn = findViewById(R.id.leftcalf_btn);
        leftfoot_btn = findViewById(R.id.leftfoot_btn);
        rightthigh_btn = findViewById(R.id.rightthigh_btn);
        rightcalf_btn = findViewById(R.id.rightcalf_btn);
        rightfoot_btn = findViewById(R.id.rightfoot_btn);


        head_btn.setOnClickListener(this);
        chest_btn.setOnClickListener(this);
        belly_btn.setOnClickListener(this);
        waist_btn.setOnClickListener(this);

        leftupperarm_btn.setOnClickListener(this);
        leftarm_btn.setOnClickListener(this);
        lefthand_btn.setOnClickListener(this);
        rightupperarm_btn.setOnClickListener(this);
        rightarm_btn.setOnClickListener(this);
        righthand_btn.setOnClickListener(this);

        leftthigh_btn.setOnClickListener(this);
        leftcalf_btn.setOnClickListener(this);
        leftfoot_btn.setOnClickListener(this);
        rightthigh_btn.setOnClickListener(this);
        rightcalf_btn.setOnClickListener(this);
        rightfoot_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_btn:
                selectBodyPart = "บริเวณหัว";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.chest_btn:
                selectBodyPart = "หน้าอก";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.belly_btn:
                selectBodyPart = "หน้าท้อง";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.waist_btn:
                selectBodyPart = "ช่วงเอว";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.leftupperarm_btn:
                selectBodyPart = "ต้นแขนซ้าย";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.leftarm_btn:
                selectBodyPart = "ปลายแขนซ้าย";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.lefthand_btn:
                selectBodyPart = "มือซ้าย";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.rightupperarm_btn:
                selectBodyPart = "ต้นแขนขวา";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.rightarm_btn:
                selectBodyPart = "ปลายแขนขวา";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.righthand_btn:
                selectBodyPart = "มือขวา";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.leftthigh_btn:
                selectBodyPart = "ต้นขาซ้าย";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.leftcalf_btn:
                selectBodyPart = "น่องซ้าย";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.leftfoot_btn:
                selectBodyPart = "เท้าซ้าย";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.rightthigh_btn:
                selectBodyPart = "ต้นขาขวา";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.rightcalf_btn:
                selectBodyPart = "น่องขวา";
                selecttxt.setText(selectBodyPart);
                break;
            case R.id.rightfoot_btn:
                selectBodyPart = "เท้าขวา";
                selecttxt.setText(selectBodyPart);
                break;
        }
    }


    private void setenviroment() {
        selecttxt = findViewById(R.id.selecttxt);
        back = findViewById(R.id.back);
        okbutton = findViewById(R.id.okbutton);
        Intent intent = getIntent();
        pat_index = intent.getIntExtra("pat_index",0);
        System.out.println("pat in body is "+pat_index);
    }

    private void backtoprevious() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent L = new Intent(BodyPartActivity.this, DoctorDiagnosisActivity.class);
                L.putExtra("pat_index",pat_index);
                L.putExtra("selectBodyPart",selectBodyPart);
                startActivity(L);
            }
        });

        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent L = new Intent(BodyPartActivity.this, DoctorDiagnosisActivity.class);
                L.putExtra("pat_index",pat_index);
                L.putExtra("selectBodyPart",selectBodyPart);
                startActivity(L);
            }
        });
    }

    private void getdata() {
        Intent intent = getIntent();
        pat_index = intent.getIntExtra("pat_index",0);
    }


}