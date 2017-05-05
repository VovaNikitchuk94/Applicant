package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstans.TimeFormTable;

import java.util.ArrayList;

public class TimeFormDBWrapper extends BaseDBWrapper {

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

                    Log.d("My", "id  ArrayList<TimeFormInfo>-> " + timeFormInfo.getId());
                    Log.d("My", "name - ArrayList<TimeFormInfo>> " + timeFormInfo.getStrTimeFormName());
                    Log.d("My", "sname  ArrayList<TimeFormInfo>-> " + timeFormInfo.getStrTimeFormLink());
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

    public void addTimeForm(TimeFormInfo timeFormInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, timeFormInfo.getContentValues());
        database.close();
    }

    public TimeFormInfo getTimeFormById(long nId) {
        TimeFormInfo timeFormInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = TimeFormTable.Cols.TIME_FORM_INFO_FIELD_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try{
            if (cursor!=null && cursor.moveToFirst()){
                timeFormInfo = new TimeFormInfo(cursor);
            }
        } finally {
            if (cursor!=null){
                cursor.close();
            }
            database.close();
        }
        return timeFormInfo;
    }
}
