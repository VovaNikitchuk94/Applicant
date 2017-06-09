package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants.CitiesTable;

import java.util.ArrayList;

public class CitiesInfoDBWrapper extends BaseDBWrapper {

    public CitiesInfoDBWrapper(Context context) {
        super(context, CitiesTable.TABLE_NAME);
    }

    public ArrayList<CitiesInfo> getAllCities() {
        ArrayList<CitiesInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        Cursor cursor = database.query(getTableName(), null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CitiesInfo citiesInfo = new CitiesInfo(cursor);
                    arrResult.add(citiesInfo);
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

    public ArrayList<CitiesInfo> getAllCitiesById(long nId) {
        ArrayList<CitiesInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CitiesInfo citiesInfo = new CitiesInfo(cursor);
                    arrResult.add(citiesInfo);

                    Log.d("My", "CitiesInfo getAllCitiesById citiesInfo.getId() - >  " + citiesInfo.getId());
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

    public void updateCity(CitiesInfo citiesInfo) {
        SQLiteDatabase database = getWritable();
        String strRequest = CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(citiesInfo.getId())};
        database.update(getTableName(), citiesInfo.getContentValues(), strRequest, arrArgs);
        database.close();
    }

    public void addCity(CitiesInfo citiesInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, citiesInfo.getContentValues());
        database.close();
    }

    public CitiesInfo getCityById(long nId) {
        CitiesInfo citiesInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = CitiesTable.Cols.CITIES_INFO_FIELD_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try{
            if (cursor!=null && cursor.moveToFirst()){
                citiesInfo = new CitiesInfo(cursor);

                Log.d("My", "CitiesInfo getCityById citiesInfo.getId() - >  " + citiesInfo.getId());
            }
        } finally {
            if (cursor!=null){
                cursor.close();
            }
            database.close();
        }
        return citiesInfo;
    }
}
