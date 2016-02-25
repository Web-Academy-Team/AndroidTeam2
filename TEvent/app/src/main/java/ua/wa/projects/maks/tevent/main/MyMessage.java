package ua.wa.projects.maks.tevent.main;


import android.support.v7.app.AppCompatActivity;

import ua.wa.projects.maks.tevent.R;

public class MyMessage extends AppCompatActivity {

    public static final String TABLE_NAME = "MyMessage";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "DateMsg";
    public static final String COLUMN_TIME = "TimeMsg";
    public static final String COLUMN_NAME = "FirstName";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_WHOT = "WhatDo";
    public static final String COLUMN_PADR = "PostADR";
    public static final String COLUMN_RMIND = "Remin";
    public static final String COLUMN_RDATE = "Rdate";
    public static final String COLUMN_RTIME = "Rtime";
    public static final String COLUMN_ALARM = "Ralarm";
    public static final String COLUMN_NOTIF = "Rnotif";

    public long id;
    public String mDateMsg;
    public String mTimeMsg;
    public String mFirstName;
    public String mPhone;
    public String mWhatDo;
    public String mPostADR;
    public int mRemind;
    public String mRdate;
    public String mRtime;
    public int mRalarm;
    public int mRnotif;

    public MyMessage(){}


    public MyMessage(String dateMsg, String timeMsg, String firstName, String phone, String whatDo,
                     String postADR, int remind, String rdate, String rtime, int ralarm, int rnotif) {
        mDateMsg = dateMsg;
        mTimeMsg = timeMsg;
        mFirstName = firstName;
        mPhone = phone;
        mWhatDo = whatDo;
        mPostADR = postADR;
        mRemind = remind;
        mRdate = rdate;
        mRtime = rtime;
        mRalarm = ralarm;
        mRnotif = rnotif;
    }

}
