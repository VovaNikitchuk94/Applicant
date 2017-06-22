package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.UniversityInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants.UniversityTable;

import java.util.ArrayList;

public class UniversitiesInfoDBWrapper extends BaseDBWrapper {

    public UniversitiesInfoDBWrapper(Context context) {
        super(context, UniversityTable.TABLE_NAME);
    }

    //TODO  как то много методов... что я пил?
    public ArrayList<UniversityInfo> getAllUniversities() {
        ArrayList<UniversityInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        Cursor cursor = database.query(getTableName(), null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    UniversityInfo universityInfo = new UniversityInfo(cursor);
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

    public ArrayList<UniversityInfo> getAllUniversitiesById(long nId) {
        ArrayList<UniversityInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CITIES_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    UniversityInfo universityInfo = new UniversityInfo(cursor);
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

    public ArrayList<UniversityInfo> getAllUniversitiesByDegree(long nId, String category) {
        ArrayList<UniversityInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequestCategoryName = UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CATEGORY_NAME + "=?";
        String strRequestCityId = UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CITIES_ID + "=?";
        String strFullRequest = strRequestCityId + " AND " + strRequestCategoryName;
        String arrArgs[] = new String[]{String.valueOf(nId), category};
        Cursor cursor = database.query(getTableName(), null, strFullRequest, arrArgs, null, null, null );
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    UniversityInfo universityInfo = new UniversityInfo(cursor);
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

    public ArrayList<UniversityInfo> getAllDegree() {
        ArrayList<UniversityInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CATEGORY_NAME + "=?";
        Cursor cursor = database.query(getTableName(), null, strRequest, new String[]{"Університети (43)"}, null, null, null );
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    UniversityInfo universityInfo = new UniversityInfo(cursor);
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

    public void updateUniversity(UniversityInfo universityInfo) {
        SQLiteDatabase database = getWritable();
        String strRequest = UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CITIES_ID + "=?" + " AND "
                + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_NAME + "=?";
        String arrArgs[] = new String[]{Long.toString(universityInfo.getLongCityId()),
                universityInfo.getStrUniversityName()};
        database.update(getTableName(), universityInfo.getContentValues(), strRequest, arrArgs);
        database.close();
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
            if (cursor != null && cursor.moveToFirst()){
                universityInfo = new UniversityInfo(cursor);
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
            database.close();
        }
        return universityInfo;
    }
}
