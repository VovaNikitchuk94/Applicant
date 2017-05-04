package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstans;
import com.example.vova.applicant.toolsAndConstans.DBConstans.UniversityTable;

import java.util.ArrayList;

public class UniversitiesInfoDBWrapper extends BaseDBWrapper {

    public UniversitiesInfoDBWrapper(Context context) {
        super(context, UniversityTable.TABLE_NAME);
    }

    public ArrayList<UniversityInfo> getAllUniversities() {
        ArrayList<UniversityInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        Cursor cursor = database.query(getTableName(), null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    UniversityInfo universityInfo = new UniversityInfo(cursor);
                    arrResult.add(universityInfo);

                    Log.d("My", "id -> " + universityInfo.getId());
                    Log.d("My", "name -> " + universityInfo.getStrUniversityName());
                    Log.d("My", "sname -> " + universityInfo.getStrUniversityLink());
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

    public void addUniversity(UniversityInfo universityInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, universityInfo.getContentValues());
        database.close();
    }

    public UniversityInfo getUniversityById(long nId) {
        UniversityInfo universityInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = UniversityTable.Cols.UNIVERSITY_INFO_FIELD_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try{
            if (cursor!=null && cursor.moveToFirst()){
                universityInfo = new UniversityInfo(cursor);
            }
        } finally {
            if (cursor!=null){
                cursor.close();
            }
            database.close();
        }
        return universityInfo;
    }
}
