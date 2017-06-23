package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.LegendInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants.LegendInfoTable;

import java.util.ArrayList;

public class LegendDBWrapper extends BaseDBWrapper {

    public LegendDBWrapper(Context context) {
        super(context, LegendInfoTable.TABLE_NAME);
    }

    public void updateLegend(LegendInfo legendInfo) {
        SQLiteDatabase database = getWritable();
        String strRequest = LegendInfoTable.Cols.LEGEND_INFO_FIELD_SPECIALITY_ID + "=?  AND "
                + LegendInfoTable.Cols.LEGEND_INFO_FIELD_NAME + "=?";
        String arrArgs[] = new String[]{Long.toString(legendInfo.getLongSpecialityId()),
                legendInfo.getStrNameLegend()};
        database.update(getTableName(), legendInfo.getContentValues(), strRequest, arrArgs);
        database.close();
    }

    public void addLegend(LegendInfo legendInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, legendInfo.getContentValues());
        database.close();
    }

    public ArrayList<LegendInfo> getLegendsById(long nId) {
        ArrayList<LegendInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = LegendInfoTable.Cols.LEGEND_INFO_FIELD_SPECIALITY_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    LegendInfo legendInfo = new LegendInfo(cursor);
                    arrResult.add(legendInfo);
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
