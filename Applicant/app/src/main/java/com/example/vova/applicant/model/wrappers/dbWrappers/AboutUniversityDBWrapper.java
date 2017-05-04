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

                    Log.d("My", "id  AboutUniversityDBWrapper detailUniverInfo.getId()-> " + aboutUniversityInfo.getId());
                    Log.d("My", "name AboutUniversityDBWrapper detailUniverInfo.getStrDetailText()-> " + aboutUniversityInfo.getStrAboutUniversType());
                    Log.d("My", "sname AboutUniversityDBWrapper detailUniverInfo.getStrDetailLink()-> " + aboutUniversityInfo.getStrAboutUniversData());

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
