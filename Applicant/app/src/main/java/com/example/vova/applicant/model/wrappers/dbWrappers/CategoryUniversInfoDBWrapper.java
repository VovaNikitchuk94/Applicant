package com.example.vova.applicant.model.wrappers.dbWrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vova.applicant.model.CategoryUniversInfo;
import com.example.vova.applicant.toolsAndConstans.DBConstants.CategoryUniversTable;

import java.util.ArrayList;

public class CategoryUniversInfoDBWrapper extends BaseDBWrapper {

    public CategoryUniversInfoDBWrapper(Context context) {
        super(context, CategoryUniversTable.TABLE_NAME);
    }

    public ArrayList<CategoryUniversInfo> getAllCategoryById(long nId) {
        ArrayList<CategoryUniversInfo> arrResult = new ArrayList<>();
        SQLiteDatabase database = getReadable();
        String strRequest = CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_CITIES_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CategoryUniversInfo categoryUniversInfo = new CategoryUniversInfo(cursor);
                    arrResult.add(categoryUniversInfo);
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

    public void updateCategory(CategoryUniversInfo categoryUniversInfo) {
        SQLiteDatabase database = getWritable();
        String strRequest = CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_CITIES_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(categoryUniversInfo.getId())};
        database.update(getTableName(), categoryUniversInfo.getContentValues(), strRequest, arrArgs);
        database.close();
    }

    public void addCategory(CategoryUniversInfo categoryUniversInfo) {
        SQLiteDatabase database = getWritable();
        database.insert(getTableName(), null, categoryUniversInfo.getContentValues());
        database.close();
    }

    public CategoryUniversInfo getCategoryById(long nId) {
        CategoryUniversInfo categoryUniversInfo = null;
        SQLiteDatabase database = getReadable();
        String strRequest = CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_CITIES_ID + "=?";
        String arrArgs[] = new String[]{Long.toString(nId)};
        Cursor cursor = database.query(getTableName(), null, strRequest, arrArgs, null, null, null );
        try{
            if (cursor!=null && cursor.moveToFirst()){
                categoryUniversInfo = new CategoryUniversInfo(cursor);
            }
        } finally {
            if (cursor!=null){
                cursor.close();
            }
            database.close();
        }
        return categoryUniversInfo;
    }
}
