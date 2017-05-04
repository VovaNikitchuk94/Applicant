package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.applicant.toolsAndConstans.DBConstans.UniversityDetailTable;

public class DetailUniverInfo extends BaseEntity {

    private String mStrDetailText;
    private String mStrDetailLink;

    public DetailUniverInfo(String strDetailText, String strDetailLink) {
        mStrDetailText = strDetailText;
        mStrDetailLink = strDetailLink;
    }

    public DetailUniverInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_ID)));
        mStrDetailText = cursor.getString(cursor.getColumnIndex(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_NAME));
        mStrDetailLink = cursor.getString(cursor.getColumnIndex(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_LINK));
    }

    public String getStrDetailText() {
        return mStrDetailText;
    }

    public void setStrDetailText(String strDetailText) {
        mStrDetailText = strDetailText;
    }

    public String getStrDetailLink() {
        return mStrDetailLink;
    }

    public void setStrDetailLink(String strDetailLink) {
        mStrDetailLink = strDetailLink;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_NAME, getStrDetailText());
        values.put(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_LINK, getStrDetailLink());
        return values;
    }
}
