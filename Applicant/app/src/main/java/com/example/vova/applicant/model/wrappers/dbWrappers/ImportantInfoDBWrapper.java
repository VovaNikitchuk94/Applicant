package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.ImportantInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstans.ImportantInfoTable;

import java.util.ArrayList;

public class ImportantInfoDBWrapper extends BaseDBWrapper {

    public ImportantInfoDBWrapper(Context context) {
        super(context, ImportantInfoTable.TABLE_NAME);
    }

    public ArrayList<ImportantInfo> getAllInfos() {
        ArrayList<ImportantInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        Cursor cursor = database.query(getTableName(), null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ImportantInfo importantInfo = new ImportantInfo(cursor);
                    arrResult.add(importantInfo);
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

    public void addInfos(ImportantInfo importantInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, importantInfo.getContentValues());
        database.close();
    }

}
