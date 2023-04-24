package com.example.tuderm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuderm.ml.Finalmodel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DoctorDiagnosisActivity extends AppCompatActivity implements RecycleViewInterface {
    int pat_index,count;
    ImageView back;
    Button next,ai,position,addposition,addimage;
    TextInputLayout symptom,treatmentandmedicine,diagnosis;
    String Sdiagnosis,Ssymptom,Streatmentandmedicine,selectBodyPart;
    TextView setname;
    DBHelper dbh;
    RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    limitimageAdapter limitimageAdapter;
    MenuBuilder menuBuilder;
    int imageSize = 224;
    int tempindex=0;
    int max = 0;
    private SharedPreferences sharedPreferences1,sharedPreferences2,sharedPreferences3,sharedPreferences4;
    private static final String TEXT_STATE1 = "text_state";
    private static final String TEXT_STATE2 = "text_state";
    private static final String TEXT_STATE3 = "text_state";
    private static final String TEXT_STATE4 = "text_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_doctor_diagnosis);
        setenviroment();
        savetext();
        getDataFromIntent();
        backtoprevious();
        getposition();
        adddiagnosisimage();
        save();
        aidiagnosis();
        setRecycle();
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        dbh = new DBHelper(this);
        pat_index = intent.getIntExtra("pat_index",0);
        setname.setText(dbh.getpatientname(pat_index));
        if(intent.getStringExtra("selectBodyPart")!=null){
            selectBodyPart = intent.getStringExtra("selectBodyPart");
            position.setText(selectBodyPart);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor1 = getSharedPreferences("sharedPref1", MODE_PRIVATE).edit();
        String savedText1 = symptom.getEditText().getText().toString();
        editor1.putString(TEXT_STATE1, savedText1);
        editor1.apply();

        SharedPreferences.Editor editor2 = getSharedPreferences("sharedPref2", MODE_PRIVATE).edit();
        String savedText2 = treatmentandmedicine.getEditText().getText().toString();
        editor2.putString(TEXT_STATE2, savedText2);
        editor2.apply();

        SharedPreferences.Editor editor3 = getSharedPreferences("sharedPref3", MODE_PRIVATE).edit();
        String savedText3 = diagnosis.getEditText().getText().toString();
        editor3.putString(TEXT_STATE3, savedText3);
        editor3.apply();

        SharedPreferences.Editor editor4 = getSharedPreferences("sharedPref4", MODE_PRIVATE).edit();
        String savedText4 = position.getText().toString();
        editor4.putString(TEXT_STATE4, savedText4);
        editor4.apply();
    }

    private void savetext() {
        sharedPreferences1 = getSharedPreferences("sharedPref1", MODE_PRIVATE);
        String savedText1 = sharedPreferences1.getString(TEXT_STATE1, "");
        symptom.getEditText().setText(savedText1);

        sharedPreferences2 = getSharedPreferences("sharedPref2", MODE_PRIVATE);
        String savedText2 = sharedPreferences2.getString(TEXT_STATE2, "");
        treatmentandmedicine.getEditText().setText(savedText2);

        sharedPreferences3 = getSharedPreferences("sharedPref3", MODE_PRIVATE);
        String savedText3 = sharedPreferences3.getString(TEXT_STATE3, "");
        diagnosis.getEditText().setText(savedText3);

        sharedPreferences4 = getSharedPreferences("sharedPref4", MODE_PRIVATE);
        String savedText4 = sharedPreferences4.getString(TEXT_STATE4, "");
        position.setText(savedText4);

        //set txt for demo
        symptom.getEditText().setText("ผื่นแดงบวม");
        treatmentandmedicine.getEditText().setText("H1-Antihistamine");
        diagnosis.getEditText().setText("โรคลมพิษเฉียบพลัน");
    }

    private void aidiagnosis() {
        final int[] count = {0};
        ai = findViewById(R.id.aibtn);
        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = dbh.getTempimagedata();
                cursor.moveToFirst();
                count[0] = 0;
                if(cursor.getCount() > 0){
                    do{
                        byte[] image = cursor.getBlob(2);
                        String text = cursor.getString(3);
                        System.out.println("text ="+ text);
                        System.out.println("count ="+ count[0]);
                        if(text==null){
                            System.out.println("ok null");
                            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                            classifyImage(bitmap, count[0]);
                        }
                        count[0]++;
                    }while(cursor.moveToNext());
                    Intent L = new Intent(DoctorDiagnosisActivity.this, AIDiagnosisActivity.class);
                    startActivity(L);
                    setRecycle();
                }else{
                    Toast.makeText(view.getContext(), "กรุณาเพิ่มรูปเพื่อวิเคราะห์ด้วยAI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void classifyImage(Bitmap bitmap,int count) {

        try {
            Finalmodel model = Finalmodel.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*3);
            //in size of image. 4 is number of byte's float (4byte) , imageSize*imageSize is for all pixel and 3 is r g b value in each pixel=

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            System.out.println(width+" "+height);
            if (width != imageSize || height != imageSize) {
                // resize the Bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
                System.out.println("after "+width+" "+height);
            }


            byteBuffer.order(ByteOrder.nativeOrder());
            int[] value = new int[imageSize*imageSize];
            bitmap.getPixels(value,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
            int pixel=0;

            //iterate over each pixel,rgb value
            //Add those values individually to the bytebuffer
            for(int i = 0; i <imageSize ; i++){
                for(int j = 0; j<imageSize; j++){
                    int val= value[pixel++]; // RGB value
                    byteBuffer.putFloat(((val >> 16) & 0xFF)*(1.f/1.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF)*(1.f/1.f));
                    byteBuffer.putFloat((val & 0xFF)*(1.f/1.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Finalmodel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences=outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos=0;
            float maxConfidence=0;
            for(int i=0;i<confidences.length;i++){
                if(confidences[i]>maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes={"โรคสิวหัวช้าง","โรคผิวหนังอักเสบระยะเรื้อรัง ","โรครอยกระในผู้สูงอายุ","ผิวปกติ","โรคสะเก็ดเงินผื่นหนาเฉพาะที่","โรคลมพิษเฉียบพลัน"};
            //res.setText(classes[maxPos]); //most confidence
            String aidiagnosis = classes[maxPos];

            System.out.println("index "+count+" "+aidiagnosis);
            dbh.updateTempImagedata(aidiagnosis,count);

            String s ="";
            for(int i=0;i<classes.length;i++){
                s +=String.format("%s: %.1f%%\n",classes[i],confidences[i]*100);
            }
            System.out.println(s);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @SuppressLint("RestrictedApi")
    private void adddiagnosisimage() {
        menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.popupimagemenu, menuBuilder);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuPopupHelper optionmenu = new MenuPopupHelper(DoctorDiagnosisActivity.this,menuBuilder,view);
                optionmenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu_opencamera:
                                ImagePicker.with(DoctorDiagnosisActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .cameraOnly()
                                        .start();
                                return true;

                            case R.id.menu_opengallery:
                                ImagePicker.with(DoctorDiagnosisActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .galleryOnly()
                                        .start();
                                return true;
                        }
                        return false;
                    }
                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {
                    }
                });
                optionmenu.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getExtras()  != null){
            Uri uri = data.getData();
            byte[] imageBytes = null;
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > -1 ) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                imageBytes = baos.toByteArray();
                dbh.addTempImagedata(imageBytes,max);
                max++;
                dbh.rearrange();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setRecycle();
        }
    }

    private void setRecycle() {
        ArrayList<imageModel> imageModelArrayList = new ArrayList<>();
        //getdata
        Cursor cursor = dbh.getTempimagedata();
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do{
                int index = cursor.getInt(0);
                byte[] image = cursor.getBlob(2);
                String text = cursor.getString(3);
                imageModelArrayList.add(new imageModel(index,image,text));
            }while(cursor.moveToNext());
            sqLiteDatabase = dbh.getReadableDatabase();
            limitimageAdapter = new limitimageAdapter(this,R.layout.row_image_withmenu, imageModelArrayList,sqLiteDatabase,this);
            //set layout
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setAdapter(limitimageAdapter);
            recyclerView.setLayoutManager((new LinearLayoutManager(this,recyclerView.HORIZONTAL,false)));
        }
    }

    private void getposition() {
        addposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sdiagnosis = diagnosis.getEditText().getText().toString();
                Ssymptom = symptom.getEditText().getText().toString();
                Streatmentandmedicine = treatmentandmedicine.getEditText().getText().toString();
                Intent L = new Intent(DoctorDiagnosisActivity.this, BodyPartActivity.class);
                L.putExtra("pat_index",pat_index);
                startActivity(L);
            }
        });
    }


    private void save() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!diagnosis.getEditText().getText().toString().equals("")){
                    Sdiagnosis = diagnosis.getEditText().getText().toString(); //edittext
                }else{
                    Toast.makeText(DoctorDiagnosisActivity.this,"กรุณาระบุผลวินิจฉัย",Toast.LENGTH_SHORT).show();
                    count++;
                }
                if(!symptom.getEditText().getText().toString().equals("")){
                    Ssymptom = symptom.getEditText().getText().toString(); //edittext
                }else{
                    Toast.makeText(DoctorDiagnosisActivity.this,"กรุณาระบุอาการโรคผิวหนัง",Toast.LENGTH_SHORT).show();
                    count++;
                }
                Streatmentandmedicine = treatmentandmedicine.getEditText().getText().toString();
                if(count==0){
                    senddata();
                }
                count=0;
            }
        });

    }

    private void senddata() {
        //get now time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy", new Locale("th", "TH"));
        String cDate = sdf.format(new Date());
        sdf = new SimpleDateFormat("HH:mm", new Locale("th", "TH"));
        String cTime = sdf.format(new Date());
        DBHelper dbh = new DBHelper(this);
        dbh.adddiagnosisdata(Sdiagnosis,Ssymptom,Streatmentandmedicine,cDate,cTime,selectBodyPart,pat_index);
        int dia_index = dbh.getdiagnosisfid(); //get dia id
        System.out.println("dia is"+dia_index);
        dbh.addDiaIDtoTemp(dia_index);
        dbh.clearTemp(); //clear temp image
        tempindex=0;
        max=0;
        treatmentandmedicine.getEditText().setText(null);
        symptom.getEditText().setText(null);
        position.setText(null);
        diagnosis.getEditText().setText(null);
        Toast.makeText(DoctorDiagnosisActivity.this,"บันทึกข้อมูลการรักษาแล้ว",Toast.LENGTH_SHORT).show();
        Intent L = new Intent(DoctorDiagnosisActivity.this, MainPageActivity.class);
        startActivity(L);
    }

    private void backtoprevious() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(DoctorDiagnosisActivity.this, PatientOptionActivity.class);
                L.putExtra("pat_index",pat_index);
                dbh.clearTemp();
                tempindex=0;
                max=0;
                treatmentandmedicine.getEditText().setText(null);
                symptom.getEditText().setText(null);
                position.setText(null);
                diagnosis.getEditText().setText(null);

                startActivity(L);
            }
        });
    }

    private void setenviroment() {
        addposition = findViewById(R.id.addposition);
        position = findViewById(R.id.positiontxt);
        back = findViewById(R.id.back);
        diagnosis = findViewById(R.id.diagnosistxt);
        symptom = findViewById(R.id.symptomtxt);
        treatmentandmedicine = findViewById(R.id.treatmentandmedicinetxt);
        next = findViewById(R.id.nextbtn);
        addimage = findViewById(R.id.addimage);
        setname = findViewById(R.id.setname);

        //set txt for demo
        symptom.getEditText().setText("ผื่นแดงบวม");
        treatmentandmedicine.getEditText().setText("H1-Antihistamine");
        diagnosis.getEditText().setText("โรคลมพิษเฉียบพลัน");
    }

    @Override
    public void onItemClick(int position) {
        System.out.println("injava");
    }
}