package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vova.applicant.db.DBHelper;
import com.example.vova.applicant.model.BaseEntity;

import java.util.ArrayList;

public abstract class BaseDBWrapper<T extends BaseEntity>  {

    private DBHelper mDBHelper;
    private String mStrTableName;
    private String mStrRequest;
    private String mStrArrArgs[];


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

    public String getStrRequest() {
        return mStrRequest;
    }

    public void setStrRequest(String strRequest) {
        mStrRequest = strRequest;
    }

    public String[] getStrArrArgs() {
        return mStrArrArgs;
    }

    public void setStrArrArgs(String[] strArrArgs) {
        mStrArrArgs = strArrArgs;
    }

//    public void addItem(T ItemInfo) {
//        SQLiteDatabase database = getWritable();
//        database.insert(getTableName(), null, ItemInfo.getContentValues());
//        database.close();
//    }

    public void addAllItems(ArrayList<T> items) {
        SQLiteDatabase database = getWritable();
        try {
            database.beginTransaction();
            for (T item : items) {
                database.insert(getTableName(), null, item.getContentValues());
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    public void updateAllItems(ArrayList<T> items) {
        SQLiteDatabase database = getWritable();
        try {
            database.beginTransaction();
            for (T item : items) {
                database.update(getTableName(), item.getContentValues(), getStrRequest(), getStrArrArgs());
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    //TODO что делать с курсором?
//    public ArrayList<T> getAllItemsById(long nId) {
//        ArrayList<T> arrResult = new ArrayList<>();
//        SQLiteDatabase database = getReadable();
//        Cursor cursor = database.query(getTableName(), null, getStrRequest(), getStrArrArgs(), null, null, null);
//        try {
//            if (cursor != null && cursor.moveToFirst()) {
//                do {
////                    how to add?
////                    arrResult.add(new T());
//                } while (cursor.moveToNext());
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            database.close();
//        }
//        return arrResult;
//    }
}
