package com.example.tuderm;

public class diagnosisModel {
    private byte[] profileimage;
    private String diagnosisbydoctor,datetime;
    private int index;

    public diagnosisModel(int index, byte[] profileimage, String diagnosisbydoctor, String datetime){
        this.index=index;
        this.profileimage=profileimage;
        this.diagnosisbydoctor = diagnosisbydoctor;
        this.datetime = datetime;
    }

    public byte[] getDiaimage() {
        return profileimage;
    }

    public void setDiaimage(byte[] profileimage) {
        this.profileimage = profileimage;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getdiagnosisbydoctor() {
        return diagnosisbydoctor;
    }

    public void setdiagnosisbydoctor(String diagnosisbydoctor) {
        this.diagnosisbydoctor = diagnosisbydoctor;
    }

    public String getdatetime() {
        return datetime;
    }

    public void setdatetime(String datetime) {
        this.datetime = datetime;
    }
}
