package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstans.AboutUniversityTable;

public class AboutUniversityInfo extends BaseEntity implements Parcelable{

    private long mLongDetailUNVId;
    private String mStrAboutUniversType;
    private String mStrAboutUniversData;

    public AboutUniversityInfo(long detailUnvId, String strAboutUniversType, String strAboutUniversData) {
        mLongDetailUNVId = detailUnvId;
        mStrAboutUniversType = strAboutUniversType;
        mStrAboutUniversData = strAboutUniversData;
    }

    public AboutUniversityInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_ID)));
        mLongDetailUNVId = cursor.getLong(cursor.getColumnIndex(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DETAIL_UNV_ID));
        mStrAboutUniversType = cursor.getString(cursor.getColumnIndex(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_TYPE));
        mStrAboutUniversData = cursor.getString(cursor.getColumnIndex(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DATA));
    }

    private AboutUniversityInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongDetailUNVId = parcel.readLong();
        mStrAboutUniversType = parcel.readString();
        mStrAboutUniversData = parcel.readString();
    }

    public String getStrAboutUniversType() {
        return mStrAboutUniversType;
    }

    public void setStrAboutUniversType(String strAboutUniversType) {
        mStrAboutUniversType = strAboutUniversType;
    }

    public String getStrAboutUniversData() {
        return mStrAboutUniversData;
    }

    public void setStrAboutUniversData(String strAboutUniversData) {
        mStrAboutUniversData = strAboutUniversData;
    }

    public long getLongDetailUNVId() {
        return mLongDetailUNVId;
    }

    public void setLongDetailUNVId(long longDetailUNVId) {
        mLongDetailUNVId = longDetailUNVId;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DETAIL_UNV_ID, getLongDetailUNVId());
        values.put(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_TYPE, getStrAboutUniversType());
        values.put(AboutUniversityTable.Cols.ABOUT_UNIVERSITY_INFO_FIELD_DATA, getStrAboutUniversData());
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(mLongDetailUNVId);
        dest.writeString(mStrAboutUniversType);
        dest.writeString(mStrAboutUniversData);

    }

    public static final Parcelable.Creator<AboutUniversityInfo> CREATOR
            = new Parcelable.Creator<AboutUniversityInfo>() {
        public AboutUniversityInfo createFromParcel(Parcel in) {
            return new AboutUniversityInfo(in);
        }

        public AboutUniversityInfo[] newArray(int size) {
            return new AboutUniversityInfo[size];
        }
    };
}
