package com.example.vova.applicant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vova.applicant.toolsAndConstans.DBConstants;
import com.example.vova.applicant.toolsAndConstans.DBConstants.AboutUniversityTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.ApplicationTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.ApplicationsTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.CategoryUniversTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.CitiesTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.ImportantInfoTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.LegendInfoTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.SpecialitiesTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.TimeFormTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.UniversityDetailTable;
import com.example.vova.applicant.toolsAndConstans.DBConstants.UniversityTable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + CitiesTable.TABLE_NAME
                + " (" + CitiesTable.Cols.CITIES_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID + " INTEGER NOT NULL, "
                + CitiesTable.Cols.CITIES_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + CitiesTable.Cols.CITIES_INFO_FIELD_LINK + " TEXT NOT NULL, "
                + CitiesTable.Cols.CITIES_INFO_FIELD_DATE_UPDATE + " TEXT NOT NULL, "
                + CitiesTable.Cols.CITIES_INFO_FIELD_FAVORITE + " TEXT NOT NULL); ");

        sqLiteDatabase.execSQL("CREATE TABLE " + CategoryUniversTable.TABLE_NAME
                + " (" + CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_CITIES_ID + " INTEGER NOT NULL, "
                + CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_LINK + " TEXT NOT NULL, "
                + CategoryUniversTable.Cols.CATEGORY_UNIVERS_INFO_FIELD_DATE_UPDATE + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + UniversityTable.TABLE_NAME
                + " (" + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CITIES_ID + " INTEGER NOT NULL, "
                + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CATEGORY_NAME + " TEXT NOT NULL, "
                + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_CATEGORY_LINK + " TEXT NOT NULL, "
                + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_LINK + " TEXT NOT NULL, "
                + UniversityTable.Cols.UNIVERSITY_INFO_FIELD_FAVORITE + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + UniversityDetailTable.TABLE_NAME
                + " (" + UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_UNV_ID + " INTEGER NOT NULL, "
                + UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_LINK + " TEXT NOT NULL, "
                + UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_DATE_UPDATE + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + AboutUniversityTable.TABLE_NAME
                + " (" + AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DETAIL_UNV_ID + " INTEGER NOT NULL, "
                + AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_TYPE + " TEXT NOT NULL, "
                + AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DATA + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + TimeFormTable.TABLE_NAME
                + " (" + TimeFormTable.Cols.TIME_FORM_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DETAIL_UNV_ID + " INTEGER NOT NULL, "
                + TimeFormTable.Cols.TIME_FORM_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + TimeFormTable.Cols.TIME_FORM_INFO_FIELD_LINK + " TEXT NOT NULL, "
                + TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DATE_UPDATE + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + SpecialitiesTable.TABLE_NAME
                + " (" + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_TIME_FORM_ID + " INTEGER NOT NULL, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_DEGREE + " INTEGER NOT NULL, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_SPECIALITY + " TEXT NOT NULL, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_APPLICATION + " TEXT, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ORDERS + " TEXT, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_EXAMS + " TEXT, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_LINK + " TEXT NOT NULL, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_DATE_UPDATE + " TEXT NOT NULL, "
                + SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_FAVORITE + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + ApplicationsTable.TABLE_NAME
                + " (" + DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_SPECIALITY_ID + " INTEGER NOT NULL, "

                //applicant detail info
                + ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_NUMBER + " INTEGER, "
                + ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_PRIORITY + " TEXT, "
                + ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_TOTAL_SCORE + " TEXT, "
                + ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_MARK_DOCUMENT + " TEXT, "
                + ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_MARK_TEST + " TEXT, "
                + ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_ORIGINAL_DOCUMENT + " TEXT,"
                + ApplicationsTable.Cols.APPLICATION_INFO_FIELD_FULL_DATA + " TEXT NOT NULL, "

                + ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_LINK + " TEXT, "
                + ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_BACKGROUND + " TEXT, "
                + ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_DATE_UPDATE + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + ApplicationTable.TABLE_NAME
                + " (" + ApplicationTable.Cols.APPLICATION_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME + " TEXT NOT NULL); ");

        sqLiteDatabase.execSQL("CREATE TABLE " + ImportantInfoTable.TABLE_NAME
                + " (" + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALITY_ID + " INTEGER NOT NULL, "

                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_UNIVERSITY_INFOS + " TEXT, "
//                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALITY + " TEXT, "
//                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALIZATION + " TEXT, "
//                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_FACULTY + " TEXT, "
//                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_TIME_FORM + " TEXT, "
//                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_LAST_TIME_UPDATE + " TEXT, "

                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_NUMBER + " TEXT, "
                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_NAME + " TEXT NOT NULL, "
                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_PRIORITY + " TEXT, "
                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_TOTAL_SCORE + " TEXT, "
                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_MARK_DOCUMENT + " TEXT, "
                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_MARK_TEST + " TEXT, "
                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_ORIGINAL_DOCUMENT + " TEXT,"
                + ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_FULL_DATA + " TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + LegendInfoTable.TABLE_NAME
                + " (" + LegendInfoTable.Cols.LEGEND_INFO_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LegendInfoTable.Cols.LEGEND_INFO_FIELD_SPECIALITY_ID + " INTEGER NOT NULL, "
                + LegendInfoTable.Cols.LEGEND_INFO_FIELD_NAME + " TEXT, "
                + LegendInfoTable.Cols.LEGEND_INFO_FIELD_DETAIL + " TEXT, "
                + LegendInfoTable.Cols.LEGEND_INFO_FIELD_BACKGROUND + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
