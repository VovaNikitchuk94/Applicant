package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vova.applicant.model.SpecialtiesInfo;

import com.example.vova.applicant.toolsAndConstans.DBConstans.SpecialitiesTable;

import java.util.ArrayList;

public class SpecialityDBWrapper extends BaseDBWrapper {

    public SpecialityDBWrapper(Context context) {
        super(context, SpecialitiesTable.TABLE_NAME);
    }

    public ArrayList<SpecialtiesInfo> getAllSpecialities() {
        ArrayList<SpecialtiesInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        Cursor cursor = database.query(getTableName(), null, null, null, null, null, null);
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

    public ArrayList<SpecialtiesInfo> getAllSpecialitiesById(long nId) {
        ArrayList<SpecialtiesInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_TIME_FORM_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
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
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try{
            if (cursor!=null && cursor.moveToFirst()){
                specialtiesInfo = new SpecialtiesInfo(cursor);
            }
        } finally {
            if (cursor!=null){
                cursor.close();
            }
            database.close();
        }
        return specialtiesInfo;
    }
}
