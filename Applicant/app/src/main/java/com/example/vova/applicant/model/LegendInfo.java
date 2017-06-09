package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstants.LegendInfoTable;

public class LegendInfo extends BaseEntity implements Parcelable{

    private long mLongYearId;
    private String mStrNameLegend;
    private String mStrDetailLegend;
    private String mStrBackgroundLegend;

    public LegendInfo(long longYearId, String strNameLegend, String strDetailLegend, String strBackgroundLegend) {
        mLongYearId = longYearId;
        mStrNameLegend = strNameLegend;
        mStrDetailLegend = strDetailLegend;
        mStrBackgroundLegend = strBackgroundLegend;
    }

    public LegendInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(LegendInfoTable.Cols.LEGEND_INFO_FIELD_ID)));
        mLongYearId = cursor.getLong(cursor.getColumnIndex(LegendInfoTable.Cols.LEGEND_INFO_FIELD_YEAR_ID));
        mStrNameLegend = cursor.getString(cursor.getColumnIndex(LegendInfoTable.Cols.LEGEND_INFO_FIELD_NAME));
        mStrDetailLegend = cursor.getString(cursor.getColumnIndex(LegendInfoTable.Cols.LEGEND_INFO_FIELD_DETAIL));
        mStrBackgroundLegend = cursor.getString(cursor.getColumnIndex(LegendInfoTable.Cols.LEGEND_INFO_FIELD_BACKGROUND));
    }

    public LegendInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongYearId = parcel.readLong();
        mStrNameLegend = parcel.readString();
        mStrDetailLegend = parcel.readString();
        mStrBackgroundLegend = parcel.readString();
    }

    public long getLongYearId() {
        return mLongYearId;
    }

    public void setLongYearId(long longYearId) {
        mLongYearId = longYearId;
    }

    public String getStrNameLegend() {
        return mStrNameLegend;
    }

    public void setStrNameLegend(String strNameLegend) {
        mStrNameLegend = strNameLegend;
    }

    public String getStrDetailLegend() {
        return mStrDetailLegend;
    }

    public void setStrDetailLegend(String strDetailLegend) {
        mStrDetailLegend = strDetailLegend;
    }

    public String getStrBackgroundLegend() {
        return mStrBackgroundLegend;
    }

    public void setStrBackgroundLegend(String strBackgroundLegend) {
        mStrBackgroundLegend = strBackgroundLegend;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(LegendInfoTable.Cols.LEGEND_INFO_FIELD_YEAR_ID, getLongYearId());
        values.put(LegendInfoTable.Cols.LEGEND_INFO_FIELD_NAME, getStrNameLegend());
        values.put(LegendInfoTable.Cols.LEGEND_INFO_FIELD_DETAIL, getStrDetailLegend());
        values.put(LegendInfoTable.Cols.LEGEND_INFO_FIELD_BACKGROUND, getStrBackgroundLegend());
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
        dest.writeString(mStrNameLegend);
        dest.writeString(mStrDetailLegend);
        dest.writeString(mStrBackgroundLegend);
    }

    public static final Creator<LegendInfo> CREATOR = new Creator<LegendInfo>() {
        @Override
        public LegendInfo createFromParcel(Parcel in) {
            return new LegendInfo(in);
        }

        @Override
        public LegendInfo[] newArray(int size) {
            return new LegendInfo[size];
        }
    };
}
