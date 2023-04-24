package com.example.tuderm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class StatActivity extends AppCompatActivity {

    ImageView back, filter;
    MenuBuilder menuBuilder;
    String selectdatemonthyear = "";
    DBHelper dbh = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_stat);
        setenviroment();
        defualtdata();
        backtoprevious();
        setfilter();
    }

    @SuppressLint("RestrictedApi")
    private void setfilter() {
        menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.popupfilterdata, menuBuilder);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuPopupHelper optionmenu = new MenuPopupHelper(StatActivity.this, menuBuilder, view);
                optionmenu.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_day:
                                showDateMonthYearPicker();
                                return true;
                            case R.id.menu_month:
                                showMonthYearPicker();
                                return true;
                            case R.id.menu_year:
                                showYearPicker();
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

    private void showDateMonthYearPicker() {
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
                selectdatemonthyear = sdf.format(c.getTime());
                getdata(selectdatemonthyear,1);
            }
        };
        new DatePickerDialog(StatActivity.this, dateSetListener, year, month, day).show();
    }

    public void showMonthYearPicker() {
        // Inflate the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.popup_month_year, null);

        // Get references to the number pickers
        NumberPicker monthPicker = dialogView.findViewById(R.id.month_picker);
        NumberPicker yearPicker = dialogView.findViewById(R.id.year_picker);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        Cursor cursor = dbh.getChartdata();
        cursor.moveToFirst();
        int maxyear = 0;
        int minyear = 99999;
        if (cursor.getCount() > 0) {
            do {
                String fulldate = cursor.getString(4);
                String[] split = fulldate.split("/");
                String m = split[1];
                String y = split[2];
                int nowyear = Integer.parseInt(y);
                if(nowyear>maxyear){
                    maxyear=nowyear;
                }
                if(nowyear<minyear){
                    minyear=nowyear;
                }
            } while (cursor.moveToNext());
        }

        Month.initMonth();
        monthPicker.setMaxValue(Month.getMonthArraylist().size()-1);
        monthPicker.setMinValue(0);
        monthPicker.setDisplayedValues(Month.monthname());

        // Set up listeners for the number pickers to update the selected values
        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //System.out.println(Month.getMonthArraylist().get(newVal).getname());
            }
        });

        yearPicker.setMaxValue(maxyear);
        yearPicker.setMinValue(minyear);
        yearPicker.setValue(year);

        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal);
            }
        });

        // Create the dialog and show it
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String monthValue = Month.getMonthArraylist().get(monthPicker.getValue()).getname();
                        String yearValue = Integer.toString(yearPicker.getValue());
                        String s = monthValue +"/"+ yearValue;
                        getdata(s,2);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void showYearPicker() {
        // Inflate the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.popup_year, null);

        // Get references to the number pickers
        NumberPicker yearPicker = dialogView.findViewById(R.id.year_picker);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        Cursor cursor = dbh.getChartdata();
        cursor.moveToFirst();
        int maxyear = 0;
        int minyear = 99999;
        if (cursor.getCount() > 0) {
            do {
                String fulldate = cursor.getString(4);
                String[] split = fulldate.split("/");
                String y = split[2];
                int nowyear = Integer.parseInt(y);
                if(nowyear>maxyear){
                    maxyear=nowyear;
                }
                if(nowyear<minyear){
                    minyear=nowyear;
                }
            } while (cursor.moveToNext());
        }

        yearPicker.setMaxValue(maxyear);
        yearPicker.setMinValue(minyear);
        yearPicker.setValue(year);

        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                System.out.println(newVal);
            }
        });

        // Create the dialog and show it
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String yearValue = Integer.toString(yearPicker.getValue());
                        getdata(yearValue,3);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void getdata(String select,int choice) {
        System.out.println("getdata "+select);
        dbh.setSimilalityCountToNUll();
        PieChart pieChart = findViewById(R.id.piechart);
        int acn = 0, ato = 0, lig = 0, pso = 0, seb = 0;
        Cursor cursor = dbh.getChartdata();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                String diagnosis = cursor.getString(1);
                String fulldate = cursor.getString(4);
                String[] split = fulldate.split("/");
                String d = split[0];
                String m = split[1];
                String y = split[2];
                if(choice == 1){
                    if (fulldate.equals(select)) {
                        if (diagnosis != null) {
                            Cursor c = dbh.getSimilalityData();
                            c.moveToFirst();
                            do{
                                String temp=c.getString(1);
                                if (diagnosis.toLowerCase().contains(temp.toLowerCase())) {
                                    System.out.println("match "+temp+" "+diagnosis);
                                    int i = c.getInt(4);
                                    i++;
                                    dbh.addSimilalityCount(i,c.getInt(0));
                                }
                            }while (c.moveToNext());
                        }
                    }
                }
                if(choice == 2){
                    String monthyear = m+"/"+y;
                    if (monthyear.equals(select)) {
                        if (diagnosis != null) {
                            Cursor c = dbh.getSimilalityData();
                            c.moveToFirst();
                            do{
                                String temp=c.getString(1);
                                if (diagnosis.toLowerCase().contains(temp.toLowerCase())) {
                                    System.out.println("match "+temp+" "+diagnosis);
                                    int i = c.getInt(4);
                                    i++;
                                    dbh.addSimilalityCount(i,c.getInt(0));
                                }
                            }while (c.moveToNext());
                        }
                    }

                }
                if(choice == 3){

                    if (y.equals(select)) {
                        if (diagnosis != null) {
                            Cursor c = dbh.getSimilalityData();
                            c.moveToFirst();
                            do{
                                String temp=c.getString(1);
                                if (diagnosis.toLowerCase().contains(temp.toLowerCase())) {
                                    System.out.println("match "+temp+" "+diagnosis);
                                    int i = c.getInt(4);
                                    i++;
                                    dbh.addSimilalityCount(i,c.getInt(0));
                                }
                            }while (c.moveToNext());
                        }

                    }
                }
            }while (cursor.moveToNext());
        }

        ArrayList<PieEntry> diagnosislist = new ArrayList<>();
        Cursor cg = dbh.getSimilalityData();
        cg.moveToFirst();
        do{
            if (cg.getInt(4)>0) {
                diagnosislist.add(new PieEntry(cg.getInt(4), cg.getString(3)));
            }

        }while (cg.moveToNext());
        PieDataSet pieDataSet = new PieDataSet(diagnosislist, "");
        int[] colors = new int[]{
                Color.parseColor("#FFC107"),
                Color.parseColor("#F44336"),
                Color.parseColor("#9C27B0"),
                Color.parseColor("#03A9F4"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#607D8B"),
                Color.parseColor("#FF9800"),
                Color.parseColor("#E91E63"),
                Color.parseColor("#673AB7"),
                Color.parseColor("#00BCD4"),
                Color.parseColor("#8BC34A"),
                Color.parseColor("#9E9E9E"),
                Color.parseColor("#FF5722"),
                Color.parseColor("#2196F3"),
                Color.parseColor("#CDDC39"),
                Color.parseColor("#795548"),
                Color.parseColor("#FFEB3B"),
                Color.parseColor("#3F51B5"),
                Color.parseColor("#9E9E9E"),
                Color.parseColor("#00BCD4")
        };
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(20f);

        // Disable labels
        //pieChart.setDrawEntryLabels(false);

        // Set data
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        // Convert to percent
        pieChart.setUsePercentValues(true);

        // Use %
        pieDataSet.setValueFormatter(new PercentFormatter());

        // Set center text
        pieChart.setCenterText("สถิติผลตรวจจากแพทย์");

        // Disable chart description
        pieChart.getDescription().setEnabled(false);

        // Animate chart
        pieChart.animate();

        // Get legend and set alignment and orientation
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        //legend.setXEntrySpace(10f);
        //legend.setYEntrySpace(5f);
        legend.setYOffset(0f);
        legend.setTextSize(12f);

        // Set label font size
        pieDataSet.setValueTextSize(16f);
        pieChart.invalidate();

    }

    private void defualtdata() {
        PieChart pieChart = findViewById(R.id.piechart);
        dbh.setSimilalityCountToNUll();
        Cursor cursor = dbh.getChartdata();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                String diagnosis = cursor.getString(1);
                if (diagnosis != null) {
                    Cursor c = dbh.getSimilalityData();
                    c.moveToFirst();
                    do{
                        String temp=c.getString(1);
                        if (diagnosis.toLowerCase().contains(temp.toLowerCase())) {
                            System.out.println("match "+temp+" "+diagnosis);
                            int i = c.getInt(4);
                            i++;
                            dbh.addSimilalityCount(i,c.getInt(0));
                        }
                    }while (c.moveToNext());
                }
            } while (cursor.moveToNext());
        }


        ArrayList<PieEntry> diagnosislist = new ArrayList<>();
        Cursor cg = dbh.getSimilalityData();
        cg.moveToFirst();
        do{
            if (cg.getInt(4)>0) {
                diagnosislist.add(new PieEntry(cg.getInt(4), cg.getString(3)));
            }

        }while (cg.moveToNext());


        PieDataSet pieDataSet = new PieDataSet(diagnosislist, "");
        int[] colors = new int[]{Color.parseColor("#FFC107"), Color.parseColor("#F44336"), Color.parseColor("#9C27B0"), Color.parseColor("#03A9F4"), Color.parseColor("#4CAF50")};
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(20f);

        // Disable labels
        //pieChart.setDrawEntryLabels(false);

        // Set data
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        // Convert to percent
        pieChart.setUsePercentValues(true);

        // Use %
        pieDataSet.setValueFormatter(new PercentFormatter());

        // Set center text
        pieChart.setCenterText("สถิติผลตรวจจากแพทย์");

        // Disable chart description
        pieChart.getDescription().setEnabled(false);

        // Animate chart
        pieChart.animate();

        // Get legend and set alignment and orientation
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        //legend.setXEntrySpace(10f);
        //legend.setYEntrySpace(5f);
        legend.setYOffset(0f);
        legend.setTextSize(12f);

        // Set label font size
        pieDataSet.setValueTextSize(16f);

        pieChart.invalidate();

    }

    private void backtoprevious() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent L = new Intent(StatActivity.this, MainPageActivity.class);
                startActivity(L);
            }
        });
    }

    private void setenviroment() {
        back = findViewById(R.id.back);
        filter = findViewById(R.id.filter);
    }

}