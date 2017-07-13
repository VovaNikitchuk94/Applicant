package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants;
import com.example.vova.applicant.toolsAndConstans.DBConstants.UniversityDetailTable;

import java.util.ArrayList;

public class DetailUniverDBWrapper extends BaseDBWrapper<DetailUniverInfo> {

    public DetailUniverDBWrapper(Context context) {
        super(context, UniversityDetailTable.TABLE_NAME);
    }

    public ArrayList<DetailUniverInfo> getAllDetailUnivers() {
        ArrayList<DetailUniverInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        Cursor cursor = database.query(getTableName(), null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    DetailUniverInfo detailUniverInfo = new DetailUniverInfo(cursor);
                    arrResult.add(detailUniverInfo);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return arrResult;
    }

    public ArrayList<DetailUniverInfo> getAllDetailUniversById(long nId) {
        ArrayList<DetailUniverInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_UNV_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    DetailUniverInfo detailUniverInfo = new DetailUniverInfo(cursor);
                    arrResult.add(detailUniverInfo);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return arrResult;
    }

    public void updateDetailUniver(DetailUniverInfo detailUniverInfo) {
        SQLiteDatabase database = getWritable();
        String strRequest = UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_UNV_ID + "=?" + " AND "
                + UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_NAME + "=?";
        String arrArgs[] = new String[]{Long.toString(detailUniverInfo.getLongUniversityId()),
                detailUniverInfo.getStrDetailText()};
        database.update(getTableName(), detailUniverInfo.getContentValues(), strRequest, arrArgs);
        database.close();
    }

    public void addDetailUniver(DetailUniverInfo detailUniverInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, detailUniverInfo.getContentValues());
        database.close();
    }

    public DetailUniverInfo getDetailUniverById(long nId) {
        DetailUniverInfo detailUniverInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_UNV_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try{
            if (cursor!=null && cursor.moveToFirst()){
                detailUniverInfo = new DetailUniverInfo(cursor);
            }
        } finally {
            if (cursor!=null){
                cursor.close();
            }
            database.close();
        }
        return detailUniverInfo;
    }

    @Override
    public void addAllItems(ArrayList<DetailUniverInfo> detailUniverItems) {
        super.addAllItems(detailUniverItems);
    }

    @Override
    public void updateAllItems(ArrayList<DetailUniverInfo> detailUniverItems) {

        String strRequest = UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_UNV_ID + "=?" + " AND "
                + UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_NAME + "=?";
        for (DetailUniverInfo detailUniverInfo: detailUniverItems) {
            String arrArgs[] = new String[]{Long.toString(detailUniverInfo.getLongUniversityId()),
                    detailUniverInfo.getStrDetailText()};
            setStrArrArgs(arrArgs);
        }
        setStrRequest(strRequest);
        Log.d("My", "getStrRequest -> " + getStrRequest());
        Log.d("My", "getStrArrArgs -> " + getStrArrArgs().length);

        super.updateAllItems(detailUniverItems);
    }
}
