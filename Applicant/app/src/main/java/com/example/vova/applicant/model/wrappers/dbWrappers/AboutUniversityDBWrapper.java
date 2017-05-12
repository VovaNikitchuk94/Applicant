package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vova.applicant.model.AboutUniversityInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstans.AboutUniversityTable;

import java.util.ArrayList;

public class AboutUniversityDBWrapper extends BaseDBWrapper {

    public AboutUniversityDBWrapper(Context context) {
        super(context, AboutUniversityTable.TABLE_NAME);
    }

    public ArrayList<AboutUniversityInfo> getAboutAllUnivesities() {
        ArrayList<AboutUniversityInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        Cursor cursor = database.query(getTableName(), null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    AboutUniversityInfo aboutUniversityInfo = new AboutUniversityInfo(cursor);
                    arrResult.add(aboutUniversityInfo);
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

    public ArrayList<AboutUniversityInfo> getAboutAllUnivesitiesById(long nId) {
        ArrayList<AboutUniversityInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DETAIL_UNV_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    AboutUniversityInfo universityInfo = new AboutUniversityInfo(cursor);
                    arrResult.add(universityInfo);
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

    public void updateAboutUniversity(AboutUniversityInfo aboutUniversityInfo) {
        SQLiteDatabase database = getWritable();
        String strRequest = AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DETAIL_UNV_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(aboutUniversityInfo.getId())};
        database.update(getTableName(), aboutUniversityInfo.getContentValues(), strRequest, arrArgs);
        database.close();
    }

    public void addAboutUniversity(AboutUniversityInfo aboutUniversityInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, aboutUniversityInfo.getContentValues());
        database.close();
    }

    public AboutUniversityInfo getAboutUniversityById(long nId) {
        AboutUniversityInfo aboutUniversityInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try{
            if (cursor!=null && cursor.moveToFirst()){
                aboutUniversityInfo = new AboutUniversityInfo(cursor);
            }
        } finally {
            if (cursor!=null){
                cursor.close();
            }
            database.close();
        }
        return aboutUniversityInfo;
    }
}
