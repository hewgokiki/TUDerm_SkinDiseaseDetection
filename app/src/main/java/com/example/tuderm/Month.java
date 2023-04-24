package com.example.tuderm;

import java.util.ArrayList;

public class Month {

    public static ArrayList<Month> MonthArraylist;
    private int id;
    private String name;

    public Month(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static void initMonth(){
        MonthArraylist = new ArrayList<>();
        Month january = new Month(0, "มกราคม");
        MonthArraylist.add(january);
        Month february = new Month(1, "กุมภาพันธ์");
        MonthArraylist.add(february);
        Month march = new Month(2, "มีนาคม");
        MonthArraylist.add(march);
        Month april = new Month(3, "เมษายน");
        MonthArraylist.add(april);
        Month may = new Month(4, "พฤษภาคม");
        MonthArraylist.add(may);
        Month june = new Month(5, "มิถุนายน");
        MonthArraylist.add(june);
        Month july = new Month(6, "กรกฎาคม");
        MonthArraylist.add(july);
        Month august = new Month(7, "สิงหาคม");
        MonthArraylist.add(august);
        Month september = new Month(8, "กันยายน");
        MonthArraylist.add(september);
        Month october = new Month(9, "ตุลาคม");
        MonthArraylist.add(october);
        Month november = new Month(10, "พฤศจิกายน");
        MonthArraylist.add(november);
        Month december = new Month(11, "ธันวาคม");
        MonthArraylist.add(december);

    }

    public static ArrayList<Month> getMonthArraylist() {
        return MonthArraylist;
    }

    public static String[] monthname(){
        String[] names = new String[MonthArraylist.size()];
        for(int i = 0 ; i < MonthArraylist.size() ; i++){
            names[i] = MonthArraylist.get(i).name;
        }
        return names;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
}
