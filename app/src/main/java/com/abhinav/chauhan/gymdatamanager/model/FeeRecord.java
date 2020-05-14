package com.abhinav.chauhan.gymdatamanager.model;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class FeeRecord {

    private int year;
    private int month;
    private int day;
    private String mDate;
    private int mAmount;
    private String mId;
    private String mName;

    public FeeRecord() {
    }

    public FeeRecord(int amount) {
        mAmount = amount;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        mDate = DateFormat.getInstance().format(new Date());
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getDate() {
        return mDate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public void setAmount(int mAmount) {
        this.mAmount = mAmount;
    }

    public int getAmount() {
        return mAmount;
    }
}
