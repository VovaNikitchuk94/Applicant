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
    private int mIsFavorite;

    public CitiesInfo(long longYearId, String strCityName, String strCityLink, String strDateLastUpdate, int isFavorite) {
        mLongYearId = longYearId;
        mStrCityName = strCityName;
        mStrCityLink = strCityLink;
        mStrDateLastUpdate = strDateLastUpdate;
        mIsFavorite = isFavorite;
    }

    public CitiesInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_ID)));
        mLongYearId = cursor.getLong(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID));
        mStrCityName = cursor.getString(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_NAME));
        mStrCityLink = cursor.getString(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_LINK));
        mStrDateLastUpdate = cursor.getString(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_DATE_UPDATE));
        mIsFavorite = cursor.getInt(cursor.getColumnIndex(CitiesTable.Cols.CITIES_INFO_FIELD_FAVORITE));
    }

    private CitiesInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongYearId = parcel.readLong();
        mStrCityName = parcel.readString();
        mStrCityLink = parcel.readString();
        mStrDateLastUpdate = parcel.readString();
        mIsFavorite = parcel.readInt();
    }

    public String getStrCityName() {
        return mStrCityName;
    }

    public String getStrCityLink() {
        return mStrCityLink;
    }

    public long getLongYearId() {
        return mLongYearId;
    }

    public String getStrDateLastUpdate() {
        return mStrDateLastUpdate;
    }

    public int getFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(int isFavorite) {
        this.mIsFavorite = isFavorite;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_YEAR_ID, getLongYearId());
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_NAME, getStrCityName());
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_LINK, getStrCityLink());
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_DATE_UPDATE, getStrDateLastUpdate());
        values.put(CitiesTable.Cols.CITIES_INFO_FIELD_FAVORITE, getFavorite());
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
        dest.writeInt(mIsFavorite);
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
