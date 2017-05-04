package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.applicant.toolsAndConstans.DBConstans.UniversityTable;

public class UniversityInfo extends BaseEntity{

    private String mStrUniversityName;
    private String mStrUniversityLink;

    public UniversityInfo(String strUniversityName, String strUniversityLink) {
        mStrUniversityName = strUniversityName;
        mStrUniversityLink = strUniversityLink;
    }

    public UniversityInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_ID)));
        mStrUniversityName = cursor.getString(cursor.getColumnIndex(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_NAME));
        mStrUniversityLink = cursor.getString(cursor.getColumnIndex(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_LINK));

    }

    public String getStrUniversityName() {
        return mStrUniversityName;
    }

    public String getStrUniversityLink() {
        return mStrUniversityLink;
    }

    public void setStrUniversityName(String strUniversityName) {
        mStrUniversityName = strUniversityName;
    }

    public void setStrUniversityLink(String strUniversityLink) {
        mStrUniversityLink = strUniversityLink;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_NAME, getStrUniversityName());
        values.put(UniversityTable.Cols.UNIVERSITY_INFO_FIELD_LINK, getStrUniversityLink());

        return values;
    }
}
