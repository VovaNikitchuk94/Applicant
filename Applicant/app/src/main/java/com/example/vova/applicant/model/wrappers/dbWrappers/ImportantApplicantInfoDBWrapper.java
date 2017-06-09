package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.ImportantInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants.ImportantInfoTable;

import java.util.ArrayList;

public class ImportantApplicantInfoDBWrapper extends BaseDBWrapper {

    public ImportantApplicantInfoDBWrapper(Context context) {
        super(context, ImportantInfoTable.TABLE_NAME);
    }

    public ArrayList<ImportantInfo> getImportantInfoById(long nId) {
        ArrayList<ImportantInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALITY_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
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

    public void addImportantInfo(ImportantInfo importantInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, importantInfo.getContentValues());
        database.close();
    }

}
