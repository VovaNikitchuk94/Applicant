package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.applicant.toolsAndConstans.DBConstans.ImportantInfoTable;

public class ImportantInfo extends BaseEntity {

    private long mLongSpecialityId;
    private String mStrName;
    private String mStrDetail;

    public ImportantInfo(long longSpecialityId, String strName, String strDetail) {
        mLongSpecialityId = longSpecialityId;
        mStrName = strName;
        mStrDetail = strDetail;
    }

    public ImportantInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_ID)));
        mLongSpecialityId = cursor.getLong(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALITY_ID));
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

    public long getLongSpecialityId() {
        return mLongSpecialityId;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALITY_ID, getLongSpecialityId());
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_NAME, getStrName());
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_DETAIL, getStrDetail());
        return values;
    }
}
