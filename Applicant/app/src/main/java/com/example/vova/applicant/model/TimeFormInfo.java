package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.applicant.toolsAndConstans.DBConstans.TimeFormTable;

public class TimeFormInfo extends BaseEntity {

    private String mStrTimeFormName;
    private String mStrTimeFormLink;

    public TimeFormInfo(String strTimeFormName, String strTimeFormLink) {
        mStrTimeFormName = strTimeFormName;
        mStrTimeFormLink = strTimeFormLink;
    }

    public TimeFormInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_ID)));
        mStrTimeFormName = cursor.getString(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_NAME));
        mStrTimeFormLink = cursor.getString(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_LINK));
    }

    public String getStrTimeFormName() {
        return mStrTimeFormName;
    }

    public void setStrTimeFormName(String strTimeFormName) {
        mStrTimeFormName = strTimeFormName;
    }

    public String getStrTimeFormLink() {
        return mStrTimeFormLink;
    }

    public void setStrTimeFormLink(String strTimeFormLink) {
        mStrTimeFormLink = strTimeFormLink;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_NAME, getStrTimeFormName());
        values.put(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_LINK, getStrTimeFormLink());
        return values;
    }
}
