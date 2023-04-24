package com.example.tuderm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Timer timer;
    DBHelper dbh = new DBHelper(this);
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        //hide action bar
        setContentView(R.layout.activity_main);

        int check = dbh.checkDefualtSimilality();
        if(check==0){
            dbh.addDefualtSimilality("หัวช้าง","โรคสิวหัวช้าง");
            dbh.addDefualtSimilality("ผิวหนังอักเสบ","โรคผิวหนังอักเสบระยะเรื้อรัง");
            dbh.addDefualtSimilality("สะเก็ดเงิน","โรคสะเก็ดเงินผื่นหนาเฉพาะที่");
            dbh.addDefualtSimilality("กระ","โรครอยกระในผู้สูงอายุ");
            dbh.addDefualtSimilality("ลมพิษ","โรคลมพิษเฉียบพลัน");
        }

        check = dbh.checkDefualtDisease();
        if(check==0){
            InputStream inputStream = getResources().openRawResource(R.drawable.acne);
            byte[] imageBytes = new byte[0];
            try {
                imageBytes = getBytes(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dbh.addDefualtDisease("โรคสิวหัวช้าง","สิวเป็นโรคของต่อมไขมัน sebaceous ของผิวหนังที่พบได้บ่อยที่สุดในเวชปฏิบัติ ซึ่งสิวซีสต์ (cyst) เป็นหนึ่งในสิวอักเสบที่มีขนาดใหญ่ มีลักษณะเป็นถุงใต้ผิวหนัง ภายในมีหนองหรือสารเหลว ๆ คล้ายเนย หายแล้วมักมีแผลเป็นหลงเหลืออยู่",imageBytes);

            inputStream = getResources().openRawResource(R.drawable.eczema);
            try {
                imageBytes = getBytes(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dbh.addDefualtDisease("โรคผิวหนังอักเสบระยะเรื้อรัง","eczema หรือ dermatitis หรือผิวหนังอักเสบ เป็นอาการแสดงสถานะของผิวหนัง\n" +
                    "ยังไม่ได้เป็นชื่อโรคโดยสมบูรณ์ โดยอาจเกิดจากสาเหตุภายนอกร่างกาย เช่น การสัมผัสกับสารที่ระคายเคืองหรือสารที่ร่างกายมีภูมิแพ้ หรือเกิดจากการเปลี่ยนแปลงภายในร่างกาย เช่น การเป็นภูมิแพ้โดยกำเนิด หรือปฏิกิริยาในระบบภูมิคุ้มกันร่างกาย",imageBytes);

            inputStream = getResources().openRawResource(R.drawable.psoriasis);
            try {
                imageBytes = getBytes(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dbh.addDefualtDisease("โรคสะเก็ดเงินผื่นหนาเฉพาะที่","โรคสะเก็ดเงินเป็นโรคผิวหนังที่พบบ่อย โดยถือว่าเป็นโรคที่พันธุกรรมมีผลในการทำให้เกิดโรค รอยโรคสะเก็ดเงินที่พบบ่อยที่สุดคือ \n" +
                    "Plaque-type psoriasis หรือ chronic stationary psoriasis โดยพบมากกว่าร้อยละ 80 ของผู้ป่วย ลักษณะของรอยโรคผื่นมีลักษณะหนา ขอบเขตชัดเจน มีสีแดงสด และมีสีขาวขุ่นหรือเป็นมันคล้ายสีเงิน เมื่อใช้เล็บหรือไม้ขูดจะเป็นสีขาวหลุดร่วง หากขูดแรงจะมีจุดเลือดออกใต้ผิวหนัง เนื่องจากมีเลือดมากกว่าผิวปกติ",imageBytes);

            inputStream = getResources().openRawResource(R.drawable.lentigo);
            try {
                imageBytes = getBytes(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dbh.addDefualtDisease("โรครอยกระในผู้สูงอายุ","รอยกระในผู้สูงอายุ เป็นความผิดปกติของเม็ดสีแบบ Hyperpigmentation ชนิด Hypermelanosis ที่มีการเพิ่มขึ้นของจำนวน melanin หรือเซลล์สร้างเม็ดสีที่ผิวหนัง จากการทำงานเพิ่มขึ้นหรือแบ่งตัวเพิ่มขึ้น มีลักษณะเป็นรอยดำรูปร่างไม่สม่ำเสมอ  ขนาดมักเล็กกว่า 5 มม. แต่บางครั้งรวมเป็นผื่นใหญ่ ขอบชัดแต่ไม่เรียบ \n" +
                    "อาจมีลักษณะผิวผู้สูงอายุตามส่วนต่าง ๆ ของร่างกายร่วมด้วย คือ รอยย่น รอยขาว",imageBytes);

            inputStream = getResources().openRawResource(R.drawable.urticaria);
            try {
                imageBytes = getBytes(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dbh.addDefualtDisease("โรคลมพิษเฉียบพลัน","โรคลมพิษ เป็นกลุ่มอาการที่เกิดจากปฏิกิริยาของผิวหนังต่อสาเหตุต่าง ๆ โดยลมพิษเฉียบพลันเป็นผื่นลมพิษที่ระยะเวลาเป็นไม่เกิน 6 สัปดาห์ มีลักษณะเป็นผื่นปื้นนูนแดง (wheal) แต่ละผื่นมักอยู่ไม่เกิน 24 ชั่วโมง แล้วหายไปโดยไม่มีร่องรอยเหลืออยู่ แต่ขณะที่ผื่นหนึ่งยุบก็มีผื่นอื่นขึ้นตามที่ต่าง ๆ ของร่างกาย เกิดขึ้นเองเกือบทุกวัน และมักเริ่มเกิดอย่างรวดเร็ว ในเวลานาทีหรือชั่วโมง ",imageBytes);
        }


        timer = new Timer();
        int resDat = dbh.checkPasswordData();
        if(resDat==0){ //case register
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent R = new Intent(MainActivity.this, DoctorRegisterActivity.class);
                    startActivity(R);
                    finish();
                }
            },2000); //set 2 sec delay to close logo page
        }else{
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent L = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(L);
                    finish();
                }
            },2000); //set 2 sec delay to close logo page
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}