package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.ApplicationInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants;
import com.example.vova.applicant.toolsAndConstans.DBConstants.ApplicationTable;

import java.util.ArrayList;

public class ApplicationDBWrapper extends BaseDBWrapper<ApplicationInfo> {

    public ApplicationDBWrapper(Context context) {
        super(context, ApplicationTable.TABLE_NAME);
    }

    @Override
    public void addAllItems(ArrayList<ApplicationInfo> applicationItem) {
        super.addAllItems(applicationItem);
    }

//    @Override
//    public void updateAllItems(ArrayList<ApplicationInfo> applicationItem) {
//
//        String strRequest = ApplicationTable.Cols.APPLICATION_INFO_FIELD_APPLICATIONS_ID + "=?";
//        for (ApplicationInfo applicationInfo : applicationItem) {
//            String arrArgs[] = new String[]{Long.toString(applicationInfo.getLongApplicationsId())};
//            setStrArrArgs(arrArgs);
//        }
//        setStrRequest(strRequest);
//        super.updateAllItems(applicationItem);
//    }

    public ArrayList<ApplicationInfo> getAllApplicationById(long nId) {
        ArrayList<ApplicationInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = ApplicationTable.Cols.APPLICATION_INFO_FIELD_APPLICATIONS_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ApplicationInfo applicationInfo = new ApplicationInfo(cursor);
                    arrResult.add(applicationInfo);
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
}
