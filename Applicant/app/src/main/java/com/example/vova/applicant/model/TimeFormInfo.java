package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstans.TimeFormTable;

public class TimeFormInfo extends BaseEntity implements Parcelable {

    private long mLongDetailUNVId;
    private String mStrTimeFormName;
    private String mStrTimeFormLink;

    public TimeFormInfo(long longDetailUNVId, String strTimeFormName, String strTimeFormLink) {
        mLongDetailUNVId = longDetailUNVId;
        mStrTimeFormName = strTimeFormName;
        mStrTimeFormLink = strTimeFormLink;
    }

    public TimeFormInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_ID)));
        mLongDetailUNVId = cursor.getLong(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DETAIL_UNV_ID));
        mStrTimeFormName = cursor.getString(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_NAME));
        mStrTimeFormLink = cursor.getString(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_LINK));
    }

    private TimeFormInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongDetailUNVId = parcel.readLong();
        mStrTimeFormName = parcel.readString();
        mStrTimeFormLink = parcel.readString();
    }

    public String getStrTimeFormName() {
        return mStrTimeFormName;
    }

    public void setStrTimeFormName(String strTimeFormName) {
        mStrTimeFormName = strTimeFormName;
    }

    public String getStrTimeFormLink() {
        return mStrTimeFormLink;
    }

    public void setStrTimeFormLink(String strTimeFormLink) {
        mStrTimeFormLink = strTimeFormLink;
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
        values.put(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DETAIL_UNV_ID, getLongDetailUNVId());
        values.put(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_NAME, getStrTimeFormName());
        values.put(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_LINK, getStrTimeFormLink());
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
        dest.writeString(mStrTimeFormName);
        dest.writeString(mStrTimeFormLink);
    }

    public static final Parcelable.Creator<TimeFormInfo> CREATOR
            = new Parcelable.Creator<TimeFormInfo>() {
        public TimeFormInfo createFromParcel(Parcel in) {
            return new TimeFormInfo(in);
        }

        public TimeFormInfo[] newArray(int size) {
            return new TimeFormInfo[size];
        }
    };
}
