package com.example.tuderm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddPatientActivity extends AppCompatActivity {

    Button addbirthday,addpat;
    TextView titletext;
    Button birthdaytext;
    TextInputLayout firstnametext,lastnametext,identificationnumbertext,phonenumtext,addresstext;
    DBHelper dbh;
    ImageView pic;
    FloatingActionButton  fab;
    MenuBuilder  menuBuilder;
    byte[] bytes;
    ImageView back,scantext;
    String selectedDate = ""; //defualt birthday
    String Sfirstname="",Slastname="",Sbirthday="",Stitle="",Sphonenum="",Semail="",Saddress=""; //defualt string
    int count=0; //count null

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_patient);
        setenviroment();
        backtoprevious();
        photoaddtext();
        setsourceofimage();
        settitle();
        setBirthday();
        checknull();
    }

    private void setenviroment() {
        titletext =  findViewById(R.id.filled_exposed);
        firstnametext =  findViewById(R.id.firstname);
        lastnametext =  findViewById(R.id.lastname);

        //set txt for demo
        firstnametext.getEditText().setText("วงศกร");
        lastnametext.getEditText().setText("บุญเรือง");


        birthdaytext = findViewById(R.id.birtdaybtn);
        identificationnumbertext =  findViewById(R.id.identificationnumber);
        phonenumtext =  findViewById(R.id.phonenum);
        addresstext =  findViewById(R.id.address);
        back = findViewById(R.id.back);
        addpat = findViewById(R.id.addpatientbtn);
        scantext = findViewById(R.id.scantext);
    }

    @SuppressLint("RestrictedApi")
    private void photoaddtext() {
        menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.popupimagemenu, menuBuilder);
        scantext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuPopupHelper optionmenu = new MenuPopupHelper(AddPatientActivity.this,menuBuilder,view);
                optionmenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu_opencamera:
                                ImagePicker.with(AddPatientActivity.this)
                                        .crop(16f, 9f)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .cameraOnly()
                                        .start(2);
                                return true;

                            case R.id.menu_opengallery:
                                ImagePicker.with(AddPatientActivity.this)
                                        .crop(16f, 9f)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .galleryOnly()
                                        .start(2);
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

    private void backtoprevious() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(AddPatientActivity.this, MainPageActivity.class);
                startActivity(L);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void setsourceofimage() {
        menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.popupimagemenu, menuBuilder);
        pic = findViewById(R.id.addpicture);
        setdefaultimagefordatabase();
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuPopupHelper optionmenu = new MenuPopupHelper(AddPatientActivity.this,menuBuilder,view);
                optionmenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu_opencamera:
                                ImagePicker.with(AddPatientActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .cameraOnly()
                                        .start(1);
                                return true;

                            case R.id.menu_opengallery:
                                ImagePicker.with(AddPatientActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .galleryOnly()
                                        .start(1);
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

        fab =  findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuPopupHelper optionmenu = new MenuPopupHelper(AddPatientActivity.this,menuBuilder,view);
                optionmenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu_opencamera:
                                ImagePicker.with(AddPatientActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .cameraOnly()
                                        .start(1);
                                return true;

                            case R.id.menu_opengallery:
                                ImagePicker.with(AddPatientActivity.this)
                                        .cropSquare() //Crop image to 1:1 (Optional)
                                        .compress(1024) //Final image size will be less than 1 MB(Optional)
                                        .galleryOnly()
                                        .start(1);
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

    private void setdefaultimagefordatabase() {
        pic.setImageResource(R.drawable.defaultprofileimage);
        Bitmap bitmap = ((BitmapDrawable) pic.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        bytes = stream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                if (data.getExtras() != null) {
                    Uri uri = data.getData();
                    pic.setImageURI(uri);
                    Bitmap bitmap = ((BitmapDrawable) pic.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    bytes = stream.toByteArray();
                }
            } else if (requestCode == 2) {
                if (data.getExtras() != null) {
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                        ocr(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void ocr(Bitmap bitmap) {
        System.out.println("ocr");
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionCloudTextRecognizerOptions options =
                new FirebaseVisionCloudTextRecognizerOptions.Builder()
                        .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                        .build();
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getCloudTextRecognizer(options);
        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText texts) {
                processTextRecognitionResult(texts);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void processTextRecognitionResult(FirebaseVisionText firebaseVisionText) {
        String result = "";
        if (firebaseVisionText.getTextBlocks().size() == 0) {
            System.out.println("error_not_found");
            return;
        }
        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
            result = result+block.getText()+"|";
        }
        //System.out.println(result);

        String[] parts = result.split("\\|");
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < parts.length; i++) {
            list.add(parts[i].trim());
        }

        ArrayList<String> datafromocr = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {

            //for title
            if(parts[i].contains("บัตรประจำตัวประชาชน")) {
                String input = parts[i];
                int startIndex = input.indexOf("Thai National ID Card") + "Thai National ID Card".length() + 1;
                String s = input.substring(startIndex);
                //System.out.println(s);
                s=s.trim().replaceAll("\n", "");
                datafromocr.add(s);
            }

            //for name
            if(parts[i].contains("นาย")) {
                String input = parts[i];
                int startIndex = input.indexOf("นาย");
                int endIndex = input.indexOf("Name") - 1;
                if (startIndex >= 0 && endIndex >= startIndex && endIndex <= input.length()) {
                    String s = input.substring(startIndex, endIndex).trim();
                    s=s.trim().replaceAll("\n", "");
                    datafromocr.add(s);
                }
            }
            if(parts[i].contains("นาง")) {
                String input = parts[i];
                int startIndex = input.indexOf("นาง");
                int endIndex = input.indexOf("Name") - 1;
                if (startIndex >= 0 && endIndex >= startIndex && endIndex <= input.length()) {
                    String s = input.substring(startIndex, endIndex).trim();
                    s=s.trim().replaceAll("\n", "");
                    datafromocr.add(s);
                }
            }
            //for birthday
            if(parts[i].contains("Last")) {
                String input = parts[i];
                int startIndex = input.indexOf("เกิดวันที่") + "เกิดวันที่".length() + 1;
                String s = input.substring(startIndex);
                int endIndex = s.indexOf("\n");
                s = s.substring(0, endIndex);
                s=s.trim().replaceAll("\n", "");
                datafromocr.add(s);
            }
            //for address
            if(parts[i].contains("ที่อยู่")) {
                String input = parts[i];
                int startIndex = input.indexOf("ที่อยู่");
                int endIndex = input.indexOf("\n", startIndex);
                endIndex = input.indexOf("\n", endIndex + 1);
                String s = input.substring(startIndex, endIndex);
                s=s.trim().replaceAll("\n", "");
                datafromocr.add(s);
            }
        }

        for (String item : datafromocr) {
            System.out.println("item is "+item);
        }
        identificationnumbertext.getEditText().setText(datafromocr.get(0));
        String[] splitname = datafromocr.get(1).split(" ");
        firstnametext.getEditText().setText(splitname[1]);
        lastnametext.getEditText().setText(splitname[2]);
        birthdaytext.setText(datafromocr.get(2));
        addresstext.getEditText().setText(datafromocr.get(3));
    }

    private void settitle() {
        String[] sort = new String[]{"นาย","นาง","นางสาว","ไม่ระบุ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.dropdown_item,sort);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);
    }

    private void setBirthday() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, date);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy", new Locale("th", "TH"));
                selectedDate = sdf.format(c.getTime());
                birthdaytext.setText(selectedDate);
            }
        };

        birthdaytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddPatientActivity.this, dateSetListener, year, month, day).show();
            }
        });
    }

    private void checknull() {
        addpat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!firstnametext.getEditText().getText().toString().equals("")){
                    Sfirstname = firstnametext.getEditText().getText().toString(); //edittext
                }else{
                    Toast.makeText(AddPatientActivity.this,"กรุณาใส่ข้อมูลชื่อ",Toast.LENGTH_SHORT).show();
                    count++;
                }
                if(!lastnametext.getEditText().getText().toString().equals("")){
                    Slastname = lastnametext.getEditText().getText().toString(); //edittext
                }else{
                    Toast.makeText(AddPatientActivity.this,"กรุณาใส่ข้อมูลนามสกุล",Toast.LENGTH_SHORT).show();
                    count++;
                }
                if(birthdaytext.getText()!=null){
                    Sbirthday = birthdaytext.getText().toString();
                }
                if(titletext.getText()!=null){
                    Stitle = titletext.getText().toString();
                }
                if(phonenumtext.getEditText().getText()!=null){
                    Sphonenum = phonenumtext.getEditText().getText().toString();
                }
                if(identificationnumbertext.getEditText().getText()!=null){
                    Semail = identificationnumbertext.getEditText().getText().toString();
                }
                if(addresstext.getEditText().getText()!=null){
                    Saddress = addresstext.getEditText().getText().toString();
                }

                if(count<=0){
                    count=0;
                    savedata();
                }
                count=0;
            }
        });
    }

    private void savedata() {
        //save string
        dbh = new DBHelper(this);
        int docid = dbh.getdoctorfid();
        dbh.addpatientdata(Stitle,bytes,Sfirstname,Slastname,Sbirthday,Sphonenum,Semail,Saddress,docid);
        Intent L = new Intent(AddPatientActivity.this, DoctorDiagnosisActivity.class);
        int pat_index = dbh.getlastestpatient();
        L.putExtra("pat_index",pat_index);
        Toast.makeText(AddPatientActivity.this,"บันทึกข้อมูลผู้ป่วยเรียบร้อย",Toast.LENGTH_SHORT).show();
        startActivity(L);
    }
}