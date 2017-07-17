package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;


import com.example.vova.applicant.toolsAndConstans.DBConstants;
import com.example.vova.applicant.toolsAndConstans.DBConstants.ApplicationsTable;

import java.util.ArrayList;
import java.util.List;

public class ApplicationsInfo extends BaseEntity implements Parcelable {

    private long mLongSpecialityId;
    //Detail applicant info
    private String mStrApplicantNumber;
    private String mStrApplicantName;
    private String mStrApplicantTotalScores;
    private String mStrApplicantMarkDocument;
    private String mStrApplicantMarkTest;
    private String mStrApplicantFullData;

    private String mStrApplicantLink;
    private String mStrBackground;
    private String mStrDateLastUpdate;

    public ApplicationsInfo(long longSpecialityId, String strApplicantNumber, String strApplicantName,
                            String strApplicantTotalScores, String strApplicantMarkDocument,
                            String strApplicantMarkTest, String strApplicantFullData, String strApplicantLink,
                            String strBackground, String strDateLastUpdate) {
        mLongSpecialityId = longSpecialityId;

        mStrApplicantNumber = strApplicantNumber;
        mStrApplicantName = strApplicantName;
        mStrApplicantTotalScores = strApplicantTotalScores;
        mStrApplicantMarkDocument = strApplicantMarkDocument;
        mStrApplicantMarkTest = strApplicantMarkTest;
        mStrApplicantFullData = strApplicantFullData;

        mStrApplicantLink = strApplicantLink;
        mStrBackground = strBackground;
        mStrDateLastUpdate = strDateLastUpdate;
    }

    public ApplicationsInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_ID)));
        mLongSpecialityId = cursor.getLong(cursor.getColumnIndex(DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_SPECIALITY_ID));

        mStrApplicantNumber = cursor.getString(cursor.getColumnIndex(DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_NUMBER));
        mStrApplicantName = cursor.getString(cursor.getColumnIndex(DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_NAME));
        mStrApplicantTotalScores = cursor.getString(cursor.getColumnIndex(DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_TOTAL_SCORE));
        mStrApplicantMarkDocument = cursor.getString(cursor.getColumnIndex(ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_MARK_DOCUMENT));
        mStrApplicantMarkTest = cursor.getString(cursor.getColumnIndex(ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_MARK_TEST));
        mStrApplicantFullData = cursor.getString(cursor.getColumnIndex(ApplicationsTable.Cols.APPLICATION_INFO_FIELD_FULL_DATA));

        mStrApplicantLink = cursor.getString(cursor.getColumnIndex(ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_LINK));
        mStrBackground = cursor.getString(cursor.getColumnIndex(ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_BACKGROUND));
        mStrDateLastUpdate = cursor.getString(cursor.getColumnIndex(DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_DATE_UPDATE));
    }

    public ApplicationsInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongSpecialityId = parcel.readLong();

        mStrApplicantNumber = parcel.readString();
        mStrApplicantName = parcel.readString();
        mStrApplicantTotalScores = parcel.readString();
        mStrApplicantMarkDocument = parcel.readString();
        mStrApplicantMarkTest = parcel.readString();
        mStrApplicantFullData = parcel.readString();

        mStrApplicantLink = parcel.readString();
        mStrBackground = parcel.readString();
        mStrDateLastUpdate = parcel.readString();
    }

    public long getLongSpecialityId() {
        return mLongSpecialityId;
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

    public String getStrApplicantMarkDocument() {
        return mStrApplicantMarkDocument;
    }

    public String getStrApplicantMarkTest() {
        return mStrApplicantMarkTest;
    }

    public String getStrApplicantFullData() {
        return mStrApplicantFullData;
    }

    public String getStrApplicantLink() {
        return mStrApplicantLink;
    }

    public String getStrBackground() {
        return mStrBackground;
    }

    public String getStrDateLastUpdate() {
        return mStrDateLastUpdate;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_SPECIALITY_ID, getLongSpecialityId());

        values.put(ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_NUMBER, getStrApplicantNumber());
        values.put(DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_NAME, getStrApplicantName());
        values.put(ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_TOTAL_SCORE, getStrApplicantTotalScores());
        values.put(DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_MARK_DOCUMENT, getStrApplicantMarkDocument());
        values.put(DBConstants.ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_MARK_TEST, getStrApplicantMarkTest());
        values.put(ApplicationsTable.Cols.APPLICATION_INFO_FIELD_FULL_DATA, getStrApplicantFullData());

        values.put(ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_LINK, getStrApplicantLink());
        values.put(ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_BACKGROUND, getStrBackground());
        values.put(ApplicationsTable.Cols.APPLICATIONS_INFO_FIELD_DATE_UPDATE, getStrDateLastUpdate());

        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(mLongSpecialityId);

        dest.writeString(mStrApplicantNumber);
        dest.writeString(mStrApplicantName);
        dest.writeString(mStrApplicantTotalScores);
        dest.writeString(mStrApplicantMarkDocument);
        dest.writeString(mStrApplicantMarkTest);
        dest.writeString(mStrApplicantFullData);

        dest.writeString(mStrApplicantLink);
        dest.writeString(mStrBackground);
        dest.writeString(mStrDateLastUpdate);
    }

    public static final Parcelable.Creator<ApplicationsInfo> CREATOR
            = new Parcelable.Creator<ApplicationsInfo>() {
        public ApplicationsInfo createFromParcel(Parcel in) {
            return new ApplicationsInfo(in);
        }

        public ApplicationsInfo[] newArray(int size) {
            return new ApplicationsInfo[size];
        }
    };
}
