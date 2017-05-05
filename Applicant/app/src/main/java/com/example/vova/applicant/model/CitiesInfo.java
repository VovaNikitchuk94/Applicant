package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.applicant.toolsAndConstans.DBConstans;
import com.example.vova.applicant.toolsAndConstans.DBConstans.CitiesTable;

public class CitiesInfo extends BaseEntity {

    private String mStrCityName;
    private String mStrCityLink;

    public CitiesInfo(String strCityName, String strCityLink) {
        mStrCityName = strCityName;
        mStrCityLink = strCityLink;
    }

    public CitiesInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_ID)));
        mStrCityName = cursor.getString(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_NAME));
        mStrCityLink = cursor.getString(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_LINK));

    }

    public String getStrCityName() {
        return mStrCityName;
    }

    public void setStrCityName(String strCityName) {
        mStrCityName = strCityName;
    }

    public String getStrCityLink() {
        return mStrCityLink;
    }

    public void setStrCityLink(String strCityLink) {
        mStrCityLink = strCityLink;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_NAME, getStrCityName());
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_LINK, getStrCityLink());

        return values;
    }
}
