package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.applicant.toolsAndConstans.DBConstants.ApplicationTable;

public class ApplicationInfo extends BaseEntity {

//    private long mLongApplicationsId;
    private String mName;

    public ApplicationInfo(String name) {
//        mLongApplicationsId = longApplicationsId;
        mName = name;
    }

    public ApplicationInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_ID)));
//        mLongApplicationsId = cursor.getLong(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_APPLICATIONS_ID));
        mName = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME));
    }

//    public long getLongApplicationsId() {
//        return mLongApplicationsId;
//    }

    public String getName() {
        return mName;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
//        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_APPLICATIONS_ID, getLongApplicationsId());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME, getName());
        return values;
    }
}
