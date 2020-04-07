package com.bignerdranch.practice.Model;

import java.text.DateFormat;
import java.util.Date;

public class FeeRecord {

    private String mDate;
    private int mAmount;
    private String mId;
    private String mName;

    public FeeRecord() {
    }

    public FeeRecord(int amount) {
        mAmount = amount;
        mDate = DateFormat.getDateInstance().format(new Date());
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

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int mAmount) {
        this.mAmount = mAmount;
    }
}
