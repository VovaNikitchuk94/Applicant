package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vova.applicant.model.BaseEntity;
import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants.CitiesTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.Favorite;

import java.util.ArrayList;

public class CitiesInfoDBWrapper<T extends BaseEntity> extends BaseDBWrapper<CitiesInfo> {

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
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    arrResult.add(new CitiesInfo(cursor));

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
        String strRequest = CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID + "=?"
                + " AND " + CitiesTable.Cols.CITIES_INFO_FIELD_NAME + "=?";
        String arrArgs[] = new String[]{Long.toString(citiesInfo.getLongYearId()), citiesInfo.getStrCityName()};
        database.update(getTableName(), citiesInfo.getContentValues(), strRequest, arrArgs);
        database.close();
    }

    public CitiesInfo getCityById(long nId) {
        CitiesInfo citiesInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                citiesInfo = new CitiesInfo(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return citiesInfo;
    }

    public ArrayList<CitiesInfo> getAllCitiesBySearchString(long nId, String strSearch) {
        strSearch = "%" + strSearch + "%";
        ArrayList<CitiesInfo> arrResult = new ArrayList<>();
        SQLiteDatabase db = getReadable();
        String strRequest = CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID + "=?" + " AND "
                + CitiesTable.Cols.CITIES_INFO_FIELD_NAME + " LIKE ? ";
        String arrArgs[] = new String[]{Long.toString(nId), strSearch};
        Cursor cursor = db.query(getTableName(), null, strRequest, arrArgs, null, null, null);
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
            db.close();
        }
        return arrResult;
    }

    public ArrayList<CitiesInfo> getAllFavoriteCities() {
        ArrayList<CitiesInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = CitiesTable.Cols.CITIES_INFO_FIELD_FAVORITE + "=?";
        String arrArgs[] = new String[]{Integer.toString(Favorite.FAVORITE)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null);
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

    @Override
    public void addAllItems(ArrayList<CitiesInfo> citiesItems) {
        super.addAllItems(citiesItems);
    }

    @Override
    public void updateAllItems(ArrayList<CitiesInfo> citiesItems) {

        String strRequest = CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID + "=?" + " AND "
                + CitiesTable.Cols.CITIES_INFO_FIELD_NAME + "=?";
        for (CitiesInfo citiesInfo : citiesItems) {
            String arrArgs[] = new String[]{Long.toString(citiesInfo.getLongYearId()), citiesInfo.getStrCityName()};
            setStrArrArgs(arrArgs);
        }
        setStrRequest(strRequest);
        super.updateAllItems(citiesItems);
    }
}
