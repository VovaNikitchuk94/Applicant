package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;


import com.example.vova.applicant.toolsAndConstans.DBConstans.ApplicationTable;

public class ApplicationsInfo extends BaseEntity{

    private String mStrApplicantNumber;
    private String mStrApplicantName;
    private String mStrApplicantTotalScores;
    private String mStrApplicantLink;
//    private String mStrApplicantBDO;
//    private String mStrApplicantZNO;

//    public ApplicationsInfo(String strApplicantNumber, String strApplicantName,
//                            String strApplicantTotalScores, String strApplicantBDO, String strApplicantZNO) {
//        mStrApplicantNumber = strApplicantNumber;
//        mStrApplicantName = strApplicantName;
//        mStrApplicantTotalScores = strApplicantTotalScores;
//        mStrApplicantBDO = strApplicantBDO;
//        mStrApplicantZNO = strApplicantZNO;
//    }


    public ApplicationsInfo(String strApplicantNumber, String strApplicantName, String strApplicantTotalScores,
    String link) {
        mStrApplicantNumber = strApplicantNumber;
        mStrApplicantName = strApplicantName;
        mStrApplicantTotalScores = strApplicantTotalScores;
        mStrApplicantLink = link;
    }

    public ApplicationsInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_ID)));
        mStrApplicantNumber = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NUMBER));
        mStrApplicantName = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME));
        mStrApplicantTotalScores = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_TOTAL_SCORE));
        mStrApplicantLink = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_LINK));
    }

    public String getStrApplicantNumber() {
        return mStrApplicantNumber;
    }

    public String getStrApplicantName() {
        return mStrApplicantName;
    }

    public String getStrApplicantTotalScores() {
        return mStrApplicantTotalScores;
    }

    public String getStrApplicantLink() {
        return mStrApplicantLink;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NUMBER, getStrApplicantNumber());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME, getStrApplicantName());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_TOTAL_SCORE, getStrApplicantTotalScores());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_LINK, getStrApplicantLink());
        return values;
    }
}
