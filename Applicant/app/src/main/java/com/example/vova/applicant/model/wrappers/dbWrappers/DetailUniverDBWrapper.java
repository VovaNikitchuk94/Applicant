package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vova.applicant.model.DetailUniverInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstans.UniversityDetailTable;

import java.util.ArrayList;

public class DetailUniverDBWrapper extends BaseDBWrapper {

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

                    Log.d("My", "id  detailUniverInfo.getId()-> " + detailUniverInfo.getId());
                    Log.d("My", "name detailUniverInfo.getStrDetailText()-> " + detailUniverInfo.getStrDetailText());
                    Log.d("My", "sname detailUniverInfo.getStrDetailLink()-> " + detailUniverInfo.getStrDetailLink());
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

    public void addDetailUniver(DetailUniverInfo detailUniverInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, detailUniverInfo.getContentValues());
        database.close();
    }

    public DetailUniverInfo getDetailUniverById(long nId) {
        DetailUniverInfo detailUniverInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_ID + "=?";
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
}
