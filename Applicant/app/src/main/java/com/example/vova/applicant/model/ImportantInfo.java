package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.applicant.toolsAndConstans.DBConstans.ImportantInfoTable;

public class ImportantInfo extends BaseEntity {
    private String mStrName;
    private String mStrDetail;

    public ImportantInfo(String strName, String strDetail) {
        mStrName = strName;
        mStrDetail = strDetail;
    }

    public ImportantInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_ID)));
        mStrName = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_NAME));
        mStrDetail = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_DETAIL));
    }

    public String getStrName() {
        return mStrName;
    }

    public void setStrName(String strName) {
        mStrName = strName;
    }

    public String getStrDetail() {
        return mStrDetail;
    }

    public void setStrDetail(String strDetail) {
        mStrDetail = strDetail;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_NAME, getStrName());
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_DETAIL, getStrDetail());
        return values;
    }
}
