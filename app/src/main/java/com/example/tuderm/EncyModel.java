package com.example.tuderm;

public class EncyModel {

    private byte[] image;
    private int index;
    private String text;


    public EncyModel(int index, byte[] image, String text){
        this.index=index;
        this.image=image;
        this.text=text;
    }

    public byte[] getimage() {
        return image;
    }

    public void setimage(byte[] image) {
        this.image = image;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
