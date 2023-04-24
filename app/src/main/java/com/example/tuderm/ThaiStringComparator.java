package com.example.tuderm;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class ThaiStringComparator implements Comparator<patientModel> {

    Collator collator = Collator.getInstance(new Locale("th"));

    public int compare(patientModel o1, patientModel o2) {
        return collator.compare(o1.getFullname(), o2.getFullname());
    }
}
