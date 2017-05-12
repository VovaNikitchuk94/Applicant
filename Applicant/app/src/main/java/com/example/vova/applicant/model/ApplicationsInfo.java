package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;


import com.example.vova.applicant.toolsAndConstans.DBConstans.ApplicationTable;

public class ApplicationsInfo extends BaseEntity implements Parcelable {

    private long mLongSpecialityId;
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


    public ApplicationsInfo(long specialityId, String strApplicantNumber, String strApplicantName,
                            String strApplicantTotalScores, String link) {
        mLongSpecialityId = specialityId;
        mStrApplicantNumber = strApplicantNumber;
        mStrApplicantName = strApplicantName;
        mStrApplicantTotalScores = strApplicantTotalScores;
        mStrApplicantLink = link;
    }

    public ApplicationsInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_ID)));
        mLongSpecialityId = cursor.getLong(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY_ID));
        mStrApplicantNumber = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NUMBER));
        mStrApplicantName = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME));
        mStrApplicantTotalScores = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_TOTAL_SCORE));
        mStrApplicantLink = cursor.getString(cursor.getColumnIndex(ApplicationTable.Cols.APPLICATION_INFO_FIELD_LINK));
    }

    public ApplicationsInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongSpecialityId = parcel.readLong();
        mStrApplicantNumber = parcel.readString();
        mStrApplicantName = parcel.readString();
        mStrApplicantTotalScores = parcel.readString();
        mStrApplicantLink = parcel.readString();
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

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_SPECIALITY_ID, getLongSpecialityId());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NUMBER, getStrApplicantNumber());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_NAME, getStrApplicantName());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_TOTAL_SCORE, getStrApplicantTotalScores());
        values.put(ApplicationTable.Cols.APPLICATION_INFO_FIELD_LINK, getStrApplicantLink());
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
        dest.writeString(mStrApplicantLink);

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
