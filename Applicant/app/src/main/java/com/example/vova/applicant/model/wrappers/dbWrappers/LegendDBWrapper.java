package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.LegendInfo;
import com.example.vova.applicant.model.TimeFormInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstans.LegendInfoTable;
import com.example.vova.applicant.toolsAndConstans.DBConstans.TimeFormTable;

import java.util.ArrayList;

public class LegendDBWrapper extends BaseDBWrapper {

    public LegendDBWrapper(Context context) {
        super(context, LegendInfoTable.TABLE_NAME);
    }

    public void updateLegend(LegendInfo legendInfo) {
        SQLiteDatabase database = getWritable();
        String strRequest = LegendInfoTable.Cols.LEGEND_INFO_FIELD_YEAR_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(legendInfo.getId())};
        database.update(getTableName(), legendInfo.getContentValues(), strRequest, arrArgs);
        database.close();
    }

    public void addLegend(LegendInfo legendInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, legendInfo.getContentValues());
        database.close();
    }

    public LegendInfo getLegendById(long nId) {
        LegendInfo legendInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = LegendInfoTable.Cols.LEGEND_INFO_FIELD_YEAR_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try{
            if (cursor!=null && cursor.moveToFirst()){
                legendInfo = new LegendInfo(cursor);
            }
        } finally {
            if (cursor!=null){
                cursor.close();
            }
            database.close();
        }
        return legendInfo;
    }
}
