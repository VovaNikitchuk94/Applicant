package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;


import com.example.vova.applicant.toolsAndConstans.DBConstans.ApplicationTable;

public class ApplicationsInfo extends BaseEntity implements Parcelable {

    private long mLongSpecialityId;
    private String mStrUniversity;
    private String mStrSpeciality;
    private String mStrApplicantInfo;
    private String mStrApplicantNumber;
    private String mStrApplicantName;
    private String mStrApplicantTotalScores;
    private String mStrApplicantLink;
    private String mStrBackground;

    public ApplicationsInfo(long specialityId, String strUniversity, String strSpeciality,
                            String strApplicantInfo, String strApplicantNumber,
                            String strApplicantName, String strApplicantTotalScores, String link,
                            String strBackground) {
        mLongSpecialityId = specialityId;
        mStrUniversity = strUniversity;
        mStrSpeciality = strSpeciality;
        mStrApplicantInfo = strApplicantInfo;
        mStrApplicantNumber = strApplicantNumber;
        mStrApplicantName = strApplicantName;
        mStrApplicantTotalScores = strApplicantTotalScores;
        mStrApplicantLink = link;
        mStrBackground = strBackground;
    }

    public ApplicationsInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_ID)));
        mLongSpecialityId = cursor.getLong(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY_ID));
        mStrUniversity = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_UNIVERSITY));
        mStrSpeciality = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY));
        mStrApplicantInfo = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_FIELD_INFO));
        mStrApplicantNumber = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NUMBER));
        mStrApplicantName = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME));
        mStrApplicantTotalScores = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_TOTAL_SCORE));
        mStrApplicantLink = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_LINK));
        mStrBackground = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_BACKGROUND));
    }

    public ApplicationsInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongSpecialityId = parcel.readLong();
        mStrUniversity = parcel.readString();
        mStrSpeciality = parcel.readString();
        mStrApplicantInfo = parcel.readString();
        mStrApplicantNumber = parcel.readString();
        mStrApplicantName = parcel.readString();
        mStrApplicantTotalScores = parcel.readString();
        mStrApplicantLink = parcel.readString();
        mStrBackground = parcel.readString();
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

    public long getLongSpecialityId() {
        return mLongSpecialityId;
    }

    public void setLongSpecialityId(long longSpecialityId) {
        mLongSpecialityId = longSpecialityId;
    }

    public String getStrUniversity() {
        return mStrUniversity;
    }

    public void setStrUniversity(String strUniversity) {
        mStrUniversity = strUniversity;
    }

    public String getStrSpeciality() {
        return mStrSpeciality;
    }

    public void setStrSpeciality(String strSpeciality) {
        mStrSpeciality = strSpeciality;
    }

    public String getStrApplicantInfo() {
        return mStrApplicantInfo;
    }

    public void setStrApplicantInfo(String strApplicantInfo) {
        mStrApplicantInfo = strApplicantInfo;
    }

    public String getStrBackground() {
        return mStrBackground;
    }

    public void setStrBackground(String strBackground) {
        mStrBackground = strBackground;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY_ID, getLongSpecialityId());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_UNIVERSITY, getStrUniversity());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY, getStrSpeciality());
        values.put(ApplicationTable.Cols.APPLICATION_FIELD_INFO, getStrApplicantInfo());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NUMBER, getStrApplicantNumber());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME, getStrApplicantName());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_TOTAL_SCORE, getStrApplicantTotalScores());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_LINK, getStrApplicantLink());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_BACKGROUND, getStrBackground());
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
        dest.writeString(mStrUniversity);
        dest.writeString(mStrSpeciality);
        dest.writeString(mStrApplicantInfo);
        dest.writeString(mStrApplicantNumber);
        dest.writeString(mStrApplicantName);
        dest.writeString(mStrApplicantTotalScores);
        dest.writeString(mStrApplicantLink);
        dest.writeString(mStrBackground);
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
