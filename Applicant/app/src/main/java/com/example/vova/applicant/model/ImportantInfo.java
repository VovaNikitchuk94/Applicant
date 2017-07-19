package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.vova.applicant.toolsAndConstans.DBConstants.ImportantInfoTable;

public class ImportantInfo extends BaseEntity {

    private long mLongSpecialityId;
    //University info
    private String mStrUniversityInfos;
//    private String mStrSpeciality;
//    private String mStrSpecialization;
//    private String mStrFaculty;
//    private String mStrTimeForm;
//    private String mStrLastTimeUpdate;
    //Detail applicant info
    private String mStrNumber;
    private String mStrName;
    private String mStrPriority;
    private String mStrTotalScores;
    private String mStrMarkDocument;
    private String mStrMarkTest;
    private String mStrOriginalDocument;

    private String mStrFullData;

    public ImportantInfo(long longSpecialityId,
                         String strUniversityInfos,
// String strSpeciality, String strSpecialization,
//                         String strFaculty, String strTimeForm, String strLastTimeUpdate,
                         String strNumber, String strName,
                         String strPriority, String strTotalScores, String strMarkDocument, String strMarkTest, String strOriginalDocument,
                         String strFullData) {
        mLongSpecialityId = longSpecialityId;
        mStrUniversityInfos = strUniversityInfos;
//        mStrSpeciality = strSpeciality;
//        mStrSpecialization = strSpecialization;
//        mStrFaculty = strFaculty;
//        mStrTimeForm = strTimeForm;
//        mStrLastTimeUpdate = strLastTimeUpdate;
        mStrNumber = strNumber;
        mStrName = strName;
        mStrPriority = strPriority;
        mStrTotalScores = strTotalScores;
        mStrMarkDocument = strMarkDocument;
        mStrMarkTest = strMarkTest;
        mStrOriginalDocument = strOriginalDocument;
        mStrFullData = strFullData;
    }

    public ImportantInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_ID)));
        mLongSpecialityId = cursor.getLong(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALITY_ID));

        mStrUniversityInfos = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_UNIVERSITY_INFOS));
//        mStrSpeciality = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALITY));
//        mStrSpecialization = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALIZATION));
//        mStrFaculty = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_FACULTY));
//        mStrTimeForm = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_TIME_FORM));
//        mStrLastTimeUpdate = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_LAST_TIME_UPDATE));

        mStrNumber = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_NUMBER));
        mStrName = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_NAME));
        mStrPriority = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_PRIORITY));
        mStrTotalScores = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_TOTAL_SCORE));
        mStrMarkDocument = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_MARK_DOCUMENT));
        mStrMarkTest = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_MARK_TEST));
        mStrOriginalDocument = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_ORIGINAL_DOCUMENT));
        mStrFullData = cursor.getString(cursor.getColumnIndex(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_FULL_DATA));

    }

    public long getLongSpecialityId() {
        return mLongSpecialityId;
    }

    public String getStrNumber() {
        return mStrNumber;
    }

    public String getStrName() {
        return mStrName;
    }

    public String getStrPriority() {
        return mStrPriority;
    }

    public String getStrTotalScores() {
        return mStrTotalScores;
    }

    public String getStrMarkDocument() {
        return mStrMarkDocument;
    }

    public String getStrMarkTest() {
        return mStrMarkTest;
    }

    public String getStrOriginalDocument() {
        return mStrOriginalDocument;
    }

    public String getStrUniversityInfos() {
        return mStrUniversityInfos;
    }

    //    public String getStrUniversityName() {
//        return mStrUniversityInfos;
//    }
//
//    public String getStrSpeciality() {
//        return mStrSpeciality;
//    }
//
//    public String getStrSpecialization() {
//        return mStrSpecialization;
//    }
//
//    public String getStrFaculty() {
//        return mStrFaculty;
//    }
//
//    public String getStrTimeForm() {
//        return mStrTimeForm;
//    }
//
//    public String getStrLastTimeUpdate() {
//        return mStrLastTimeUpdate;
//    }

    public String getStrFullData() {
        return mStrFullData;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALITY_ID, getLongSpecialityId());

        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_UNIVERSITY_INFOS, getStrUniversityInfos());
//        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALITY, getStrSpeciality());
//        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_SPECIALIZATION, getStrSpecialization());
//        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_FACULTY, getStrFaculty());
//        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_TIME_FORM, getStrTimeForm());
//        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_LAST_TIME_UPDATE, getStrLastTimeUpdate());

        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_NUMBER, getStrNumber());
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_NAME, getStrName());
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_PRIORITY, getStrPriority());
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_TOTAL_SCORE, getStrTotalScores());
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_MARK_DOCUMENT, getStrMarkDocument());
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_MARK_TEST, getStrMarkTest());
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_ORIGINAL_DOCUMENT, getStrOriginalDocument());
        values.put(ImportantInfoTable.Cols.IMPORTANT_INFO_FIELD_FULL_DATA, getStrFullData());

        return values;
    }
}
