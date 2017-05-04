package com.example.vova.applicant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vova.applicant.toolsAndConstans.DBConstans;
import com.example.vova.applicant.toolsAndConstans.DBConstans.AboutUniversityTable;
import com.example.vova.applicant.toolsAndConstans.DBConstans.UniversityDetailTable;
import com.example.vova.applicant.toolsAndConstans.DBConstans.UniversityTable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBConstans.DB_NAME, null, DBConstans.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + DBConstans.TABLE_NAME
                + " (" + DBConstans.CITIES_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstans.CITIES_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + DBConstans.CITIES_INFO_FIELD_LINK + " TEXT NOT NULL); ");

        sqLiteDatabase.execSQL("CREATE TABLE " + UniversityTable.TABLE_NAME
                + " (" + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_LINK + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + UniversityDetailTable.TABLE_NAME
                + " (" + UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_LINK + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + AboutUniversityTable.TABLE_NAME
                + " (" + AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_TYPE + " TEXT NOT NULL, "
                + AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DATA + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
