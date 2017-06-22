package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.ApplicationsInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants.ApplicationTable;

import java.util.ArrayList;

public class ApplicationDBWrapper extends BaseDBWrapper<ApplicationsInfo> {

    public ApplicationDBWrapper(Context context) {
        super(context, ApplicationTable.TABLE_NAME);
    }

    public ArrayList<ApplicationsInfo> getAllApplications() {
        ArrayList<ApplicationsInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        Cursor cursor = database.query(getTableName(), null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ApplicationsInfo applicationsInfo = new ApplicationsInfo(cursor);
                    arrResult.add(applicationsInfo);
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

    public ArrayList<ApplicationsInfo> getAllApplicationsById(long nId) {
        ArrayList<ApplicationsInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ApplicationsInfo applicationsInfo = new ApplicationsInfo(cursor);
                    arrResult.add(applicationsInfo);
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

    public void updateApplicant(ApplicationsInfo applicationsInfo) {
        SQLiteDatabase database = getWritable();
        String strRequest = ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY_ID + "=?" + " AND "
                + ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME + "=?";
        String arrArgs[] = new String[]{Long.toString(applicationsInfo.getLongSpecialityId()),
                applicationsInfo.getStrApplicantName()};
        database.update(getTableName(), applicationsInfo.getContentValues(), strRequest, arrArgs);
        database.close();
    }

    public void addItem(ApplicationsInfo applicationsInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, applicationsInfo.getContentValues());
        database.close();
    }

    public ApplicationsInfo getApplicationById(long nId) {
        ApplicationsInfo applicationsInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try{
            if (cursor != null && cursor.moveToFirst()){
                applicationsInfo = new ApplicationsInfo(cursor);
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
            database.close();
        }
        return applicationsInfo;
    }
}
