package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.applicant.toolsAndConstans.DBConstans.SpecialitiesTable;

public class SpecialtiesInfo extends BaseEntity{

    private String mStrSpecialty;
    private String mStrApplications;
    private String mStrAccepted;
    private String mStrAmount;
    private String mStrLink;

    public SpecialtiesInfo(String strSpecialty, String strApplications,
                           String strAccepted, String strAmount, String link) {

        mStrSpecialty = strSpecialty;
        mStrApplications = strApplications;
        mStrAccepted = strAccepted;
        mStrAmount = strAmount;
        mStrLink = link;
    }

    public SpecialtiesInfo(String strSpecialty, String strApplications,
                           String strAmount, String link) {

        mStrSpecialty = strSpecialty;
        mStrApplications = strApplications;
        mStrAmount = strAmount;
        mStrLink = link;
    }

    public SpecialtiesInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ID)));
        mStrSpecialty = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_SPECIALITY));
        mStrApplications = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_APPLICATION));
        mStrAccepted = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ACCEPTED));
        mStrAmount = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_AMOUNT));
        mStrLink = cursor.getString(cursor.getColumnIndex(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_LINK));
    }

    public String getStrSpecialty() {
        return mStrSpecialty;
    }

    public String getStrApplications() {
        return mStrApplications;
    }

    public String getStrAccepted() {
        return mStrAccepted;
    }

    public String getStrAmount() {
        return mStrAmount;
    }

    public String getStrLink() {
        return mStrLink;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_SPECIALITY, mStrSpecialty);
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_APPLICATION, mStrApplications);
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_ACCEPTED, mStrAccepted);
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_AMOUNT, mStrAmount);
        values.put(SpecialitiesTable.Cols.SPECIALITIES_INFO_FIELD_LINK, mStrLink);

        return values;
    }
}
