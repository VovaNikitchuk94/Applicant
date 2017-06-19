package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.ImportantInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants.ImportantInfoTable;

public class ImportantApplicantInfoDBWrapper extends BaseDBWrapper {

    public ImportantApplicantInfoDBWrapper(Context context) {
        super(context, ImportantInfoTable.TABLE_NAME);
    }

    public ImportantInfo getImportantInfoById(long nId) {
        ImportantInfo resultImportantInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALITY_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                resultImportantInfo = new ImportantInfo(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return resultImportantInfo;
    }

    public void addImportantInfo(ImportantInfo importantInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, importantInfo.getContentValues());
        database.close();
    }

}
