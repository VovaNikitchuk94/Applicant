package com.example.vova.applicant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vova.applicant.toolsAndConstans.DBConstans;
import com.example.vova.applicant.toolsAndConstans.DBConstans.AboutUniversityTable;
import com.example.vova.applicant.toolsAndConstans.DBConstans.ApplicationTable;
import com.example.vova.applicant.toolsAndConstans.DBConstans.CitiesTable;
import com.example.vova.applicant.toolsAndConstans.DBConstans.SpecialitiesTable;
import com.example.vova.applicant.toolsAndConstans.DBConstans.TimeFormTable;
import com.example.vova.applicant.toolsAndConstans.DBConstans.UniversityDetailTable;
import com.example.vova.applicant.toolsAndConstans.DBConstans.UniversityTable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBConstans.DB_NAME, null, DBConstans.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + CitiesTable.TABLE_NAME
                + " (" + CitiesTable.Cols.CITIES_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CitiesTable.Cols.CITIES_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + CitiesTable.Cols.CITIES_INFO_FIELD_LINK + " TEXT NOT NULL); ");

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

        sqLiteDatabase.execSQL("CREATE TABLE " + TimeFormTable.TABLE_NAME
                + " (" + TimeFormTable.Cols.TIME_FORM_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TimeFormTable.Cols.TIME_FORM_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + TimeFormTable.Cols.TIME_FORM_INFO_FIELD_LINK + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + SpecialitiesTable.TABLE_NAME
                + " (" + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_SPECIALITY + " TEXT NOT NULL, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_APPLICATION + " TEXT, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ACCEPTED + " TEXT, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_AMOUNT + " TEXT, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_LINK + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + ApplicationTable.TABLE_NAME
                + " (" + ApplicationTable.Cols.APPLICATION_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ApplicationTable.Cols.APPLICATION_INFO_FIELD_NUMBER + " INTEGER NOT NULL, "
                + ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + ApplicationTable.Cols.APPLICATION_INFO_FIELD_TOTAL_SCORE + " TEXT, "
                + ApplicationTable.Cols.APPLICATION_INFO_FIELD_LINK + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
