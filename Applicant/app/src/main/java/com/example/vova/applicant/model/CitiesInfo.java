package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstants.CitiesTable;

public class CitiesInfo extends BaseEntity implements Parcelable{

    private long mLongYearId;
    private String mStrCityName;
    private String mStrCityLink;
    private String mStrDateLastUpdate;
    private String mStrTimeLastUpdate;

    public CitiesInfo(long longYearId, String strCityName, String strCityLink, String strDateLastUpdate, String strTimeLastUpdate) {
        mLongYearId = longYearId;
        mStrCityName = strCityName;
        mStrCityLink = strCityLink;
        mStrDateLastUpdate = strDateLastUpdate;
        mStrTimeLastUpdate = strTimeLastUpdate;
    }

    public CitiesInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_ID)));
        mLongYearId = cursor.getLong(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID));
        mStrCityName = cursor.getString(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_NAME));
        mStrCityLink = cursor.getString(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_LINK));
        mStrDateLastUpdate = cursor.getString(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_DATE_UPDATE));
        mStrTimeLastUpdate = cursor.getString(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_TIME_UPDATE));
    }

    private CitiesInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongYearId = parcel.readLong();
        mStrCityName = parcel.readString();
        mStrCityLink = parcel.readString();
        mStrDateLastUpdate = parcel.readString();
        mStrTimeLastUpdate = parcel.readString();
    }

    public String getStrCityName() {
        return mStrCityName;
    }

    public void setStrCityName(String strCityName) {
        mStrCityName = strCityName;
    }

    public String getStrCityLink() {
        return mStrCityLink;
    }

    public void setStrCityLink(String strCityLink) {
        mStrCityLink = strCityLink;
    }

    public long getLongYearId() {
        return mLongYearId;
    }

    public void setLongYearId(long longYearId) {
        mLongYearId = longYearId;
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
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID, getLongYearId());
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_NAME, getStrCityName());
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_LINK, getStrCityLink());
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_LINK, getStrCityLink());
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_DATE_UPDATE, getStrDateLastUpdate());
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_TIME_UPDATE, getStrTimeLastUpdate());
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(mLongYearId);
        dest.writeString(mStrCityName);
        dest.writeString(mStrCityLink);
        dest.writeString(mStrDateLastUpdate);
        dest.writeString(mStrTimeLastUpdate);
    }

    public static final Parcelable.Creator<CitiesInfo> CREATOR
            = new Parcelable.Creator<CitiesInfo>() {
        public CitiesInfo createFromParcel(Parcel in) {
            return new CitiesInfo(in);
        }

        public CitiesInfo[] newArray(int size) {
            return new CitiesInfo[size];
        }
    };
}
