package com.example.tuderm;

public class patientModel {
    private byte[] profileimage;
    private String fullname;
    private int index;

    public patientModel(int index, byte[] profileimage, String fullname){
        this.index=index;
        this.profileimage=profileimage;
        this.fullname = fullname;
    }

    public byte[] getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(byte[] profileimage) {
        this.profileimage = profileimage;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String name) {
        this.fullname = name;
    }

}
