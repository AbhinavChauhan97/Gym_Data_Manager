package com.abhinav.chauhan.practice.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Member implements Serializable {

    private String mMemberName;
    private String mMemberPhone;
    private String mMemberAddress;
    private String mMemberJoiningDate;
    private int mNoOfFeesSubmittedMonths;
    private String mMemberId;
    private boolean mHasImage;

    public Member() {
        String memberId = FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("members")
                .document().getId();
        mMemberId = memberId;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mMemberJoiningDate = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
    }

    public Member(String name, String phone, String address) {
        this();

        mMemberName = name;
        mMemberPhone = phone;
        mMemberAddress = address;
    }

    public boolean isHasImage() {
        return mHasImage;
    }

    public void setHasImage(boolean hasImage) {
        mHasImage = hasImage;
    }

    @Exclude
    public long getDaysLeftForFeeSubmission() {

        Date joiningDate = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            joiningDate = format.parse(this.getMemberJoiningDate());

        } catch (ParseException e) {
            Log.d("db", "cant parse");
        }
        long diff = new Date().getTime() - joiningDate.getTime();

        long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        return 30 * this.getNoOfFeesSubmittedMonths() - daysDiff;
    }

    @NonNull
    @Override
    public String toString() {
        return getMemberName();
    }

    public String getMemberName() {
        return mMemberName;
    }

    public void setMemberName(String mMemberName) {
        this.mMemberName = mMemberName;
    }

    public String getMemberPhone() {
        return mMemberPhone;
    }

    public void setMemberPhone(String mMemberPhone) {
        this.mMemberPhone = mMemberPhone;
    }

    public String getMemberAddress() {
        return mMemberAddress;
    }

    public void setMemberAddress(String mMemberAddress) {
        this.mMemberAddress = mMemberAddress;
    }

    public String getMemberJoiningDate() {
        return mMemberJoiningDate;
    }

    public void setMemberJoiningDate(String mMemberJoiningDate) {
        this.mMemberJoiningDate = mMemberJoiningDate;
    }

    public int getNoOfFeesSubmittedMonths() {
        return mNoOfFeesSubmittedMonths;
    }

    public void setNoOfFeesSubmittedMonths(int mNoOfFeesSubmittedMonths) {
        this.mNoOfFeesSubmittedMonths = mNoOfFeesSubmittedMonths;
    }

    public String getMemberId() {
        return mMemberId;
    }

    public void setMemberId(String memberId) {
        this.mMemberId = memberId;
    }
}
