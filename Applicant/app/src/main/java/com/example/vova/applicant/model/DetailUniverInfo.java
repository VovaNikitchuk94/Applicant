package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstants.UniversityDetailTable;

public class DetailUniverInfo extends BaseEntity implements Parcelable {

    private long mLongUniversityId;
    private String mStrDetailText;
    private String mStrDetailLink;
    private String mStrDateLastUpdate;
    private String mStrTimeLastUpdate;

    public DetailUniverInfo(long longUniversityId, String strDetailText, String strDetailLink,
                            String strDateLastUpdate, String strTimeLastUpdate) {
        mLongUniversityId = longUniversityId;
        mStrDetailText = strDetailText;
        mStrDetailLink = strDetailLink;
        mStrDateLastUpdate = strDateLastUpdate;
        mStrTimeLastUpdate = strTimeLastUpdate;
    }

    public DetailUniverInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_ID)));
        mLongUniversityId = cursor.getLong(cursor.getColumnIndex(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_UNV_ID));
        mStrDetailText = cursor.getString(cursor.getColumnIndex(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_NAME));
        mStrDetailLink = cursor.getString(cursor.getColumnIndex(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_LINK));
        mStrDateLastUpdate = cursor.getString(cursor.getColumnIndex(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_DATE_UPDATE));
        mStrTimeLastUpdate = cursor.getString(cursor.getColumnIndex(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_TIME_UPDATE));
    }

    private DetailUniverInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongUniversityId = parcel.readLong();
        mStrDetailText = parcel.readString();
        mStrDetailLink = parcel.readString();
        mStrDateLastUpdate = parcel.readString();
        mStrTimeLastUpdate = parcel.readString();
    }

    public String getStrDetailText() {
        return mStrDetailText;
    }

    public void setStrDetailText(String strDetailText) {
        mStrDetailText = strDetailText;
    }

    public String getStrDetailLink() {
        return mStrDetailLink;
    }

    public void setStrDetailLink(String strDetailLink) {
        mStrDetailLink = strDetailLink;
    }

    public long getLongUniversityId() {
        return mLongUniversityId;
    }

    public void setLongUniversityId(long longUniversityId) {
        mLongUniversityId = longUniversityId;
    }

    public String getStrDateLastUpdate() {
        return mStrDateLastUpdate;
    }

    public String getStrTimeLastUpdate() {
        return mStrTimeLastUpdate;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_UNV_ID, getLongUniversityId());
        values.put(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_NAME, getStrDetailText());
        values.put(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_LINK, getStrDetailLink());
        values.put(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_DATE_UPDATE, getStrDateLastUpdate());
        values.put(UniversityDetailTable.Cols.UNIVERSITY_DETAIL_INFO_FIELD_TIME_UPDATE, getStrTimeLastUpdate());
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(mLongUniversityId);
        dest.writeString(mStrDetailText);
        dest.writeString(mStrDetailLink);
        dest.writeString(mStrDateLastUpdate);
        dest.writeString(mStrTimeLastUpdate);
    }

    public static final Parcelable.Creator<DetailUniverInfo> CREATOR
            = new Parcelable.Creator<DetailUniverInfo>() {
        public DetailUniverInfo createFromParcel(Parcel in) {
            return new DetailUniverInfo(in);
        }

        public DetailUniverInfo[] newArray(int size) {
            return new DetailUniverInfo[size];
        }
    };
}
