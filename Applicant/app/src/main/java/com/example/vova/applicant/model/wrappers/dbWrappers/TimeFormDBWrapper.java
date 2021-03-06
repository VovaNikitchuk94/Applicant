package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants.TimeFormTable;

import java.util.ArrayList;

public class TimeFormDBWrapper extends BaseDBWrapper<TimeFormInfo> {

    public TimeFormDBWrapper(Context context) {
        super(context, TimeFormTable.TABLE_NAME);
    }

    public ArrayList<TimeFormInfo> getAllTimeForms() {
        ArrayList<TimeFormInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        Cursor cursor = database.query(getTableName(), null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    TimeFormInfo timeFormInfo = new TimeFormInfo(cursor);
                    arrResult.add(timeFormInfo);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return arrResult;
    }

    public ArrayList<TimeFormInfo> getAllTimeFormsById(long nId) {
        ArrayList<TimeFormInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DETAIL_UNV_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    TimeFormInfo timeFormInfo = new TimeFormInfo(cursor);
                    arrResult.add(timeFormInfo);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return arrResult;
    }

    public void updateTimeForm(TimeFormInfo timeFormInfo) {
        SQLiteDatabase database = getWritable();
        String strRequest = TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DETAIL_UNV_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(timeFormInfo.getId())};
        database.update(getTableName(), timeFormInfo.getContentValues(), strRequest, arrArgs);
        database.close();
    }

    public void addTimeForm(TimeFormInfo timeFormInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, timeFormInfo.getContentValues());
        database.close();
    }

    public TimeFormInfo getTimeFormById(long nId) {
        TimeFormInfo timeFormInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DETAIL_UNV_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null);
        try{
            if (cursor != null && cursor.moveToFirst()){
                timeFormInfo = new TimeFormInfo(cursor);
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
            database.close();
        }
        return timeFormInfo;
    }

    @Override
    public void addAllItems(ArrayList<TimeFormInfo> timeFormsItems) {
        super.addAllItems(timeFormsItems);
    }

    @Override
    public void updateAllItems(ArrayList<TimeFormInfo> timeFormsItems) {

        String strRequest = TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DETAIL_UNV_ID + "=?" + " AND "
                + TimeFormTable.Cols.TIME_FORM_INFO_FIELD_NAME + "=?";

        for (TimeFormInfo timeFormInfo : timeFormsItems) {
            String arrArgs[] = new String[]{Long.toString(timeFormInfo.getLongDetailUNVId()),
                    timeFormInfo.getStrTimeFormName()};
            setStrArrArgs(arrArgs);
        }
        setStrRequest(strRequest);
        super.updateAllItems(timeFormsItems);
    }
}
