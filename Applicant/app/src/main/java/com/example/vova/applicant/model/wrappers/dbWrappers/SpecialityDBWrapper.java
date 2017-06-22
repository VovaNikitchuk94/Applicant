package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.SpecialtiesInfo;

import com.example.vova.applicant.toolsAndConstans.DBConstants.SpecialitiesTable;

import java.util.ArrayList;

public class SpecialityDBWrapper extends BaseDBWrapper {

    public SpecialityDBWrapper(Context context) {
        super(context, SpecialitiesTable.TABLE_NAME);
    }

    public ArrayList<SpecialtiesInfo> getAllSpecialitiesById(long nId) {
        ArrayList<SpecialtiesInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_TIME_FORM_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SpecialtiesInfo specialtiesInfo = new SpecialtiesInfo(cursor);
                    arrResult.add(specialtiesInfo);
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

    public ArrayList<SpecialtiesInfo> getAllSpecialitiesByIdAndDegree(long nId, long degree) {
        ArrayList<SpecialtiesInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequestFirst = SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_TIME_FORM_ID + "=?";
        String strRequestSecond = SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_DEGREE + "=?";
        String strRequest = strRequestFirst + " AND " + strRequestSecond;
        String arrArgs[] = new String[]{Long.toString(nId), Long.toString(degree)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SpecialtiesInfo specialtiesInfo = new SpecialtiesInfo(cursor);
                    arrResult.add(specialtiesInfo);
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

    public void updateSpeciality(SpecialtiesInfo specialtiesInfo) {
        SQLiteDatabase database = getWritable();
        String strRequest = SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_TIME_FORM_ID + "=?" + " AND "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_SPECIALITY + "=?";
        String arrArgs[] = new String[]{Long.toString(specialtiesInfo.getLongTimeFormId()),
                specialtiesInfo.getStrSpecialty()};
        database.update(getTableName(), specialtiesInfo.getContentValues(), strRequest, arrArgs);
        database.close();
    }

    public void addSpeciality(SpecialtiesInfo specialtiesInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, specialtiesInfo.getContentValues());
        database.close();
    }

    public SpecialtiesInfo getSpecialityById(long nId) {
        SpecialtiesInfo specialtiesInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null);
        try{
            if (cursor != null && cursor.moveToFirst()){
                specialtiesInfo = new SpecialtiesInfo(cursor);
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
            database.close();
        }
        return specialtiesInfo;
    }
}
