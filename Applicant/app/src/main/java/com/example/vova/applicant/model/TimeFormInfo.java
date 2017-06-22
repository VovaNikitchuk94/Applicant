package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstants.TimeFormTable;

public class TimeFormInfo extends BaseEntity implements Parcelable {

    private long mLongDetailUNVId;
    private String mStrTimeFormName;
    private String mStrTimeFormLink;
    private String mStrDateLastUpdate;

    public TimeFormInfo(long longDetailUNVId, String strTimeFormName, String strTimeFormLink, String strDateLastUpdate) {
        mLongDetailUNVId = longDetailUNVId;
        mStrTimeFormName = strTimeFormName;
        mStrTimeFormLink = strTimeFormLink;
        mStrDateLastUpdate = strDateLastUpdate;
    }

    public TimeFormInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_ID)));
        mLongDetailUNVId = cursor.getLong(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DETAIL_UNV_ID));
        mStrTimeFormName = cursor.getString(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_NAME));
        mStrTimeFormLink = cursor.getString(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_LINK));
        mStrDateLastUpdate = cursor.getString(cursor.getColumnIndex(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DATE_UPDATE));
    }

    private TimeFormInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongDetailUNVId = parcel.readLong();
        mStrTimeFormName = parcel.readString();
        mStrTimeFormLink = parcel.readString();
        mStrDateLastUpdate = parcel.readString();
    }

    public String getStrTimeFormName() {
        return mStrTimeFormName;
    }

    public String getStrTimeFormLink() {
        return mStrTimeFormLink;
    }

    public long getLongDetailUNVId() {
        return mLongDetailUNVId;
    }

    public String getStrDateLastUpdate() {
        return mStrDateLastUpdate;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DETAIL_UNV_ID, getLongDetailUNVId());
        values.put(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_NAME, getStrTimeFormName());
        values.put(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_LINK, getStrTimeFormLink());
        values.put(TimeFormTable.Cols.TIME_FORM_INFO_FIELD_DATE_UPDATE, getStrDateLastUpdate());
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
        dest.writeString(mStrDateLastUpdate);
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
