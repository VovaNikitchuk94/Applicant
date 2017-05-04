package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.applicant.toolsAndConstans.DBConstans.AboutUniversityTable;

public class AboutUniversityInfo extends BaseEntity {

    private String mStrAboutUniversType;
    private String mStrAboutUniversData;

    public AboutUniversityInfo(String strAboutUniversType, String strAboutUniversData) {
        mStrAboutUniversType = strAboutUniversType;
        mStrAboutUniversData = strAboutUniversData;
    }

    public AboutUniversityInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_ID)));
        mStrAboutUniversType = cursor.getString(cursor.getColumnIndex(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_TYPE));
        mStrAboutUniversData = cursor.getString(cursor.getColumnIndex(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DATA));

    }

    public String getStrAboutUniversType() {
        return mStrAboutUniversType;
    }

    public void setStrAboutUniversType(String strAboutUniversType) {
        mStrAboutUniversType = strAboutUniversType;
    }

    public String getStrAboutUniversData() {
        return mStrAboutUniversData;
    }

    public void setStrAboutUniversData(String strAboutUniversData) {
        mStrAboutUniversData = strAboutUniversData;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_TYPE, getStrAboutUniversType());
        values.put(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DATA, getStrAboutUniversData());
        return values;
    }
}
