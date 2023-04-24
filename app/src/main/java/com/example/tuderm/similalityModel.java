package com.example.tuderm;

public class similalityModel {
    private String text,disease;
    private int index;

    public similalityModel(int index, String text, String disease){
        this.index=index;
        this.text=text;
        this.disease=disease;
    }

    public String gettext() {
        return text;
    }

    public void settext(String text) {
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getdisease() {
        return disease;
    }

    public void setdisease(String disease) {
        this.disease = disease;
    }

}
