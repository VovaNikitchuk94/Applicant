package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.db.DBHelper;

public abstract class BaseDBWrapper {

    private DBHelper mDBHelper;
    private String mStrTableName;

    public BaseDBWrapper(Context context, String strTableName) {
        mDBHelper = new DBHelper(context);
        mStrTableName = strTableName;
    }

    public String getTableName() {
        return mStrTableName;
    }

    public SQLiteDatabase getWritable() {
        return mDBHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadable() {
        return mDBHelper.getReadableDatabase();
    }
}
