package com.example.tuderm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuderm.ml.Finalmodel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class QuickAIScanActivity extends AppCompatActivity {

    Button backhome;
    ImageView aiimage,back;
    TextView aitext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_quick_aiscan);
        getDataFromIntent();
        backtoprevious();
        backtohome();
    }

    private void getDataFromIntent() {
        aiimage = findViewById(R.id.aiimage);
        Bundle extras = getIntent().getExtras();
        Uri aiuri = Uri.parse(extras.getString("imageUri"));
        aiimage.setImageURI(aiuri);
        try {
            Bitmap bitmap = getBitmapFromUri(aiuri);
            classifyImage(bitmap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void classifyImage(Bitmap bitmap) {
        int imageSize =224;
        try {
            Finalmodel model = Finalmodel.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, imageSize, imageSize, 3}, DataType.FLOAT32);

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
                    byteBuffer.putFloat((val & 0xFF)*(1.f/1.f)); //recale=0
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
            aitext = findViewById(R.id.aitext);
            aitext.setText("ผลการวิเคราะห์ของ AI : \n" +classes[maxPos]); //most confidence


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

    private void backtoprevious() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aiimage.setImageURI(null);
                Intent L = new Intent(QuickAIScanActivity.this, MainPageActivity.class);
                startActivity(L);
            }
        });
    }

    private void backtohome() {
        backhome = findViewById(R.id.backhome);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiimage.setImageURI(null);
                Intent L = new Intent(QuickAIScanActivity.this, MainPageActivity.class);
                startActivity(L);
            }
        });
    }
}