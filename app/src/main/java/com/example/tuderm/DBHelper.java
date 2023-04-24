package com.example.tuderm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DBHelper extends SQLiteOpenHelper {
    //db  name
    public static final String DB_name = "App.DB";
    //Table Doctor
    private static final String DoctorTable = "Doctor";
    //Table Doctor content
    private static final String DoctorID = "doc_id";
    private static final String DoctorTitle = "doc_title";
    private static final String DoctorImage = "doc_image";
    private static final String DoctorFirstname = "doc_firstname";
    private static final String DoctorLastname = "doc_lastname";
    private static final String DoctorPassword = "doc_password";

    //Table Patient
    private static final String PatientTable = "Patient";
    //Table Patient content
    private static final String PatientID = "pat_id";
    private static final String PatientImage = "pat_image";
    private static final String PatientTitle = "pat_title";
    private static final String PatientFirstname = "pat_firstname";
    private static final String PatientLastname = "pat_lastname";
    private static final String PatientBirthday = "pat_birthday";
    private static final String PatientPhonenum = "pat_phonenum";
    private static final String PatientEmail = "pat_email";
    private static final String PatientAddress = "pat_address";
    private static final String doc_FID = "doc_fid";

    //Table DiagnosisResult
    private static final String DiagnosisTable = "DiagnosisResult";
    //Table Diagnosis content
    private static final String DiagnosisID = "dia_id";
    private static final String DiagnosisByDoctor = "dia_diagnosisbydoctor";
    private static final String DiagnosisSymptom = "dia_symptom";
    private static final String DiagnosisTreatmentandMedicine = "dia_treatmentandmedicine";
    private static final String DiagnosisDate = "dia_date";
    private static final String DiagnosisTime = "dia_time";
    private static final String DiagnosisBodyPart = "dia_bodypart";
    private static final String pat_FID = "pat_fid";

    //Table Image
    private static final String ImageTable = "Image";
    //Table Image content
    private static final String ImageID = "img_id";
    private static final String ImageFile = "img_file";
    private static final String ImageAIDiagnosis = "img_dianosisbyai";
    private static final String dia_FID = "dia_fid";

    //Table Temp Image
    private static final String TempImageTable = "TempImage";
    //Table Temp Image content
    private static final String TempImageID = "Tempimg_id";
    private static final String TempIndex = "Tempimg_Index";
    private static final String TempImageFile = "Tempimg_file";
    private static final String TempImageAIDiagnosis = "Tempimg_dianosisbyai";
    private static final String Tempdia_FID = "Tempdia_fid";

    //Table Temp Image
    private static final String SimilalityTable = "Similality";
    //Table Temp Image content
    private static final String SimilalityID = "Similality_id";
    private static final String SimilalityIndex = "Similality_index";
    private static final String SimilalityText = "Similality_text";
    private static final String SimilalityDisease = "Similality_disease";
    private static final String SimilalityCount = "Similality_count";
    //Table Disease Image content
    private static final String DiseaseTable = "Disease";
    private static final String DiseaseID = "Disease_id";
    private static final String DiseaseImage = "Disease_image";
    private static final String DiseaseName = "Disease_name";
    private static final String DiseaseInfo = "Disease_info";

    public DBHelper(Context context) {
        super(context, DB_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createdoctor = "Create TABLE "+ DoctorTable
                +"("+
                DoctorID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DoctorImage+ " BLOB,"+
                DoctorTitle+" TEXT, "+
                DoctorFirstname+" TEXT, "+
                DoctorLastname+" TEXT, "+
                DoctorPassword+" TEXT "+
                ");";

        String createpatient = "Create TABLE "+ PatientTable
                +"("+
                PatientID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                PatientImage+ " BLOB ,"+
                PatientTitle+" TEXT, "+
                PatientFirstname+" TEXT, "+
                PatientLastname+" TEXT, "+
                PatientBirthday+" TEXT, "+
                PatientPhonenum+" TEXT, "+
                PatientEmail+" TEXT, "+
                PatientAddress+" TEXT, "+
                doc_FID+" INTEGER, "+
                "FOREIGN KEY("+doc_FID+") REFERENCES "+DoctorTable+"("+DoctorID+")"+
                ");";

        String creatediagnosis = "Create TABLE "+ DiagnosisTable
                +"("+
                DiagnosisID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DiagnosisByDoctor+ " TEXT, "+
                DiagnosisSymptom+ " TEXT, "+
                DiagnosisTreatmentandMedicine+ " TEXT, "+
                DiagnosisDate+" TEXT, "+
                DiagnosisTime+" TEXT, "+
                DiagnosisBodyPart+" TEXT, "+
                pat_FID+" INTEGER, "+
                "FOREIGN KEY("+pat_FID+") REFERENCES "+PatientTable+"("+PatientID+")"+
                ");";

        String createImage = "Create TABLE "+ ImageTable
                +"("+
                ImageID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ImageFile+ " BLOB, "+
                ImageAIDiagnosis+" TEXT, "+
                dia_FID+" INTEGER "+
                ");";

        String createTempImage = "Create TABLE "+ TempImageTable
                +"("+
                TempImageID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TempIndex+ " Integer, "+
                TempImageFile+ " BLOB, "+
                TempImageAIDiagnosis+" TEXT, "+
                Tempdia_FID+" INTEGER "+
                ");";

        String createSimilality = "Create TABLE "+ SimilalityTable
                +"("+
                SimilalityID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                SimilalityText+ " TEXT, "+
                SimilalityIndex+ " TEXT, "+
                SimilalityDisease+ " TEXT, "+
                SimilalityCount + " INTEGER "+
                ");";

        String createDisease = "Create TABLE "+ DiseaseTable
                +"("+
                DiseaseID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DiseaseImage+" BLOB, "+
                DiseaseName+ " TEXT, "+
                DiseaseInfo+ " TEXT"+
                ");";

        db.execSQL(createdoctor);
        db.execSQL(createpatient);
        db.execSQL(creatediagnosis);
        db.execSQL(createImage);
        db.execSQL(createTempImage);
        db.execSQL(createSimilality);
        db.execSQL(createDisease);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop Table IF EXISTS "+DoctorTable);
        db.execSQL("Drop Table IF EXISTS "+PatientTable);
        db.execSQL("Drop Table IF EXISTS "+DiagnosisTable);
        db.execSQL("Drop Table IF EXISTS "+ImageTable);
        db.execSQL("Drop Table IF EXISTS "+TempImageTable);
        db.execSQL("Drop Table IF EXISTS "+SimilalityTable);
        db.execSQL("Drop Table IF EXISTS "+DiagnosisTable);
        onCreate(db);
    }


    //MainPage
    public int checkPasswordData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Doctor",null);
        if(cursor.getCount()==0){
            cursor.close();
            return 0;
        }else{
            cursor.close();
            return 1;
        }
    }
    //LoginPage
    public int passwordlogin(String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT doc_password FROM Doctor",null);
        cursor.moveToFirst();
        String S = cursor.getString(cursor.getColumnIndex(DoctorPassword));
        if(S.equals(password)){
            cursor.close();
            return 1;
        }else{
            cursor.close();
            return 0;
        }
    }

    public int getlastestpatient(){
        int latestPatId = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Patient ORDER BY pat_id DESC LIMIT 1;",null);
        if(cursor.moveToFirst()) {
            latestPatId = cursor.getInt(0);
        }
        cursor.close();
        return latestPatId;
    }

    public int getdiagnosisfid(){
        int latestDiagnosisId = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiagnosisResult ORDER BY dia_id DESC LIMIT 1;",null);
        if(cursor.moveToFirst()) {
            latestDiagnosisId = cursor.getInt(0);
        }
        cursor.close();
        return latestDiagnosisId;
    }

    public Cursor getdiagnosisbypatient(int patfid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiagnosisResult LEFT JOIN (SELECT *, MAX(img_id) AS img_id FROM Image GROUP BY dia_fid) AS ImageDerivedTable ON DiagnosisResult.dia_id = ImageDerivedTable.dia_fid WHERE DiagnosisResult.pat_fid = "+patfid,null);
        return cursor;
    }

    public String getdiagnosisbydiaID(int patfid,int diaid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiagnosisResult WHERE DiagnosisResult.pat_fid = "+patfid+" AND DiagnosisResult.dia_id = "+diaid,null);
        cursor.moveToFirst();
        String d = cursor.getString(cursor.getColumnIndex(DiagnosisByDoctor));
        cursor.close();
        return d;
    }

    public String getsymptombydiaID(int patfid,int diaid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiagnosisResult WHERE DiagnosisResult.pat_fid = "+patfid+" AND DiagnosisResult.dia_id = "+diaid,null);
        cursor.moveToFirst();
        String d = cursor.getString(cursor.getColumnIndex(DiagnosisSymptom));
        cursor.close();
        return d;
    }

    public String gettreatmentandmedicinebydiaID(int patfid,int diaid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiagnosisResult WHERE DiagnosisResult.pat_fid = "+patfid+" AND DiagnosisResult.dia_id = "+diaid,null);
        cursor.moveToFirst();
        String d = cursor.getString(cursor.getColumnIndex(DiagnosisTreatmentandMedicine));
        cursor.close();
        return d;
    }


    public String getpositionbydiaID(int patfid,int diaid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiagnosisResult WHERE DiagnosisResult.pat_fid = "+patfid+" AND DiagnosisResult.dia_id = "+diaid,null);
        cursor.moveToFirst();
        String d = cursor.getString(cursor.getColumnIndex(DiagnosisBodyPart));
        cursor.close();
        return d;
    }

    public Cursor getDiseasedata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM Disease",null);
        return cursor;
    }

    public Cursor getDiseaseData(int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Disease WHERE Disease.Disease_id = " + position, null);
        return cursor;
    }

    public Cursor getTempimagedata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM TempImage",null);
        return cursor;
    }

    public Cursor getSelectTempimagedata(int index){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM TempImage WHERE TempImage.Tempimg_index = "+index,null);
        return cursor;
    }

    public void addTempImagedata(byte[] imageBytes,int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(TempImageFile,imageBytes);
        c.put(TempIndex,index);
        db.insert(TempImageTable,null,c);
    }

    public void updateTempImagedata(String aidiagnosis, int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM TempImage WHERE Tempimg_Index = ? AND Tempimg_dianosisbyai IS NOT NULL";
        Cursor cursor = db.rawQuery(countQuery, new String[] { String.valueOf(index) });
        int count = cursor.getCount();
        cursor.close();
        if (count == 0) {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Tempimg_dianosisbyai", aidiagnosis);
            db.update("TempImage", values, "Tempimg_Index = ?", new String[] { String.valueOf(index) });
        }
    }

    public void rearrange() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Tempimg_Index FROM TempImage ORDER BY Tempimg_Index", null);
        int counter = 0;

        db.beginTransaction();
        try {
            while (cursor.moveToNext()) {
                int index = cursor.getInt(0);
                db.execSQL("UPDATE TempImage SET Tempimg_Index = ? WHERE Tempimg_Index = ?",
                        new Object[] {counter, index});
                counter++;
            }
            db.setTransactionSuccessful();
        } finally {
            cursor.close();
            db.endTransaction();
        }
    }
    public void addDiaIDtoTemp(int diaid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO Image (img_file, img_dianosisbyai, dia_fid) SELECT Tempimg_file, Tempimg_dianosisbyai, "+diaid+"  as Tempdia_fid FROM TempImage");
    }

    public void clearTemp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM TempImage");
    }
    public void deleteSpecifyTemp(int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM TempImage WHERE Tempimg_Index ="+index);
    }

    public void addSimilalityword(String text,String disease) {
        int si = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(SimilalityText,text);
        c.put(SimilalityDisease,disease);
        c.put(SimilalityCount,si);
        db.insert(SimilalityTable,null,c);
    }

    public void deleteSimilality(int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Similality WHERE Similality_index ="+index);
    }

    public void rearrange_similality() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Similality_index FROM Similality ORDER BY Similality_index", null);
        int counter = 0;

        db.beginTransaction();
        try {
            while (cursor.moveToNext()) {
                int index = cursor.getInt(0);
                db.execSQL("UPDATE Similality SET Similality_index = ? WHERE Similality_index = ?",
                        new Object[] {counter, index});
                counter++;
            }
            db.setTransactionSuccessful();
        } finally {
            cursor.close();
            db.endTransaction();
        }
    }

    public int checkrepeatSimilality(String text, String disease) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SimilalityTable,
                new String[]{SimilalityID},
                SimilalityText + " = ? AND " + SimilalityDisease + " = ?",
                new String[]{text, disease},
                null, null, null);

        int count = cursor.getCount();
        cursor.close();

        return count > 0 ? 1 : 0;
    }

    public Cursor getChartdata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM DiagnosisResult",null);
        return cursor;
    }

    public int checkDefualtDisease(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Disease",null);
        if(cursor.getCount()==0){
            cursor.close();
            return 0;
        }else{
            cursor.close();
            return 1;
        }
    }
    public void addDefualtDisease(String disease,String text,byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DiseaseName,disease);
        c.put(DiseaseInfo,text);
        c.put(DiseaseImage,imageBytes);
        db.insert(DiseaseTable,null,c);
    }
    public int checkDefualtSimilality(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Similality",null);
        if(cursor.getCount()==0){
            cursor.close();
            return 0;
        }else{
            cursor.close();
            return 1;
        }
    }

    public void addDefualtSimilality(String text,String disease) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(SimilalityText,text);
        c.put(SimilalityDisease,disease);
        db.insert(SimilalityTable,null,c);
    }

    public Cursor getSimilalityData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM Similality",null);
        return cursor;
    }
    public void addSimilalityCount(int i, int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(SimilalityCount, i);
        String whereClause = "Similality_id=?";
        String[] whereArgs = new String[] { String.valueOf(index) };
        db.update(SimilalityTable, c, whereClause, whereArgs);
        db.close();
    }

    public void setSimilalityCountToNUll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Similality SET Similality_count = null");
    }


    public Cursor getallimagedata(int patfid,int diaid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Image JOIN DiagnosisResult ON image.dia_fid = DiagnosisResult.dia_id WHERE DiagnosisResult.pat_fid = "+patfid+" AND DiagnosisResult.dia_id = "+diaid, null);
        return cursor;
    }

    public Cursor getpatientdata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM Patient",null);
        return cursor;
    }

    public String getpatientname(int index){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Patient WHERE pat_id = " + index,null);
        cursor.moveToFirst();
        String t = cursor.getString(cursor.getColumnIndex(PatientTitle));
        String f = cursor.getString(cursor.getColumnIndex(PatientFirstname));
        String l = cursor.getString(cursor.getColumnIndex(PatientLastname));
        String full = t+" "+f+" "+l;
        cursor.close();
        return full;
    }

    public String getdoctorname(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Doctor",null);
        cursor.moveToFirst();
        String t = cursor.getString(cursor.getColumnIndex(DoctorTitle));
        String f = cursor.getString(cursor.getColumnIndex(DoctorFirstname));
        String l = cursor.getString(cursor.getColumnIndex(DoctorLastname));
        String full = t+" "+f+" "+l;
        cursor.close();
        return full;
    }

    public Cursor getdoctordata(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Doctor",null);
        cursor.moveToFirst();
        return cursor;
    }

    public void updateDoctor(int index, byte[] imageBytes, String title, String name, String lastname, String passcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        if (imageBytes != null) {
            c.put("doc_image", imageBytes);
        }
        if (title != null && !title.isEmpty()) {
            c.put("doc_title", title);
        }
        if (name != null && !name.isEmpty()) {
            c.put("doc_firstname", name);
        }
        if (lastname != null && !lastname.isEmpty()) {
            c.put("doc_lastname", lastname);
        }
        if (passcode != null && !passcode.isEmpty()) {
            c.put("doc_password", passcode);
        }
        String whereClause = "doc_id=?";
        String[] whereArgs = new String[] { String.valueOf(index) };
        db.update(DoctorTable, c, whereClause, whereArgs);
        db.close();
        db.close();
    }

    public void updateDoctorPassword(int index, String passcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("doc_password", passcode);
        String whereClause = "doc_id=?";
        String[] whereArgs = new String[] { String.valueOf(index) };
        db.update(DoctorTable, c, whereClause, whereArgs);
        db.close();
    }

    public int getdoctorfid(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT doc_id FROM Doctor",null);
        cursor.moveToFirst();
        int i = cursor.getInt(cursor.getColumnIndex(DoctorID));
        cursor.close();
        return i;
    }

    public Bitmap getpatientpic(int index){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Patient WHERE pat_id = " + index,null);
        cursor.moveToFirst();
        byte[] profileimage = cursor.getBlob(cursor.getColumnIndex(PatientImage));
        Bitmap bitmap = BitmapFactory.decodeByteArray(profileimage,0,profileimage.length);
        cursor.close();
        return bitmap;
    }

    public Bitmap getdoctorpic(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Doctor",null);
        cursor.moveToFirst();
        byte[] profileimage = cursor.getBlob(cursor.getColumnIndex(DoctorImage));
        Bitmap bitmap = BitmapFactory.decodeByteArray(profileimage,0,profileimage.length);
        cursor.close();
        return bitmap;
    }

    public void adddiagnosisdata(String sdiagnosis, String ssymptom, String streatmentandmedicine, String currentDate, String currentTime, String bodypart, int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DiagnosisByDoctor,sdiagnosis);
        c.put(DiagnosisSymptom,ssymptom);
        c.put(DiagnosisTreatmentandMedicine,streatmentandmedicine);
        c.put(DiagnosisDate,currentDate);
        c.put(DiagnosisTime,currentTime);
        c.put(DiagnosisBodyPart,bodypart);
        c.put(pat_FID,index);
        db.insert(DiagnosisTable,null,c);
    }

    //AddPatientPage
    public void addpatientdata(String Title,byte[] imageBytes,String Firstname,String Lastname,String Birthday,String Phonenum,String Email,String Address,int fid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(PatientTitle,Title);
        c.put(PatientImage,imageBytes);
        c.put(PatientFirstname,Firstname);
        c.put(PatientLastname,Lastname);
        c.put(PatientBirthday,Birthday);
        c.put(PatientPhonenum,Phonenum);
        c.put(PatientEmail,Email);
        c.put(PatientAddress,Address);
        c.put(doc_FID,fid);
        db.insert(PatientTable,null,c);
    }

    public void adddoctordata(String Title, byte[] imageBytes, String Firstname, String Lastname,String Password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(DoctorTitle,Title);
        c.put(DoctorImage,imageBytes);
        c.put(DoctorFirstname,Firstname);
        c.put(DoctorLastname,Lastname);
        c.put(DoctorPassword,Password);
        db.insert(DoctorTable,null,c);
    }


}
