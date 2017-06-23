package com.example.vova.applicant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.vova.applicant.toolsAndConstans.DBConstants.LegendInfoTable;

public class LegendInfo extends BaseEntity implements Parcelable{

    private long mLongSpecialityId;
    private String mStrNameLegend;
    private String mStrDetailLegend;
    private String mStrBackgroundLegend;

    public LegendInfo(long longSpecialityId, String strNameLegend, String strDetailLegend, String strBackgroundLegend) {
        mLongSpecialityId = longSpecialityId;
        mStrNameLegend = strNameLegend;
        mStrDetailLegend = strDetailLegend;
        mStrBackgroundLegend = strBackgroundLegend;
    }

    public LegendInfo(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(LegendInfoTable.Cols.LEGEND_INFO_FIELD_ID)));
        mLongSpecialityId = cursor.getLong(cursor.getColumnIndex(LegendInfoTable.Cols.LEGEND_INFO_FIELD_SPECIALITY_ID));
        mStrNameLegend = cursor.getString(cursor.getColumnIndex(LegendInfoTable.Cols.LEGEND_INFO_FIELD_NAME));
        mStrDetailLegend = cursor.getString(cursor.getColumnIndex(LegendInfoTable.Cols.LEGEND_INFO_FIELD_DETAIL));
        mStrBackgroundLegend = cursor.getString(cursor.getColumnIndex(LegendInfoTable.Cols.LEGEND_INFO_FIELD_BACKGROUND));
    }

    public LegendInfo(Parcel parcel) {
        setId(parcel.readLong());
        mLongSpecialityId = parcel.readLong();
        mStrNameLegend = parcel.readString();
        mStrDetailLegend = parcel.readString();
        mStrBackgroundLegend = parcel.readString();
    }

    public long getLongSpecialityId() {
        return mLongSpecialityId;
    }

    public String getStrNameLegend() {
        return mStrNameLegend;
    }

    public String getStrDetailLegend() {
        return mStrDetailLegend;
    }

    public String getStrBackgroundLegend() {
        return mStrBackgroundLegend;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(LegendInfoTable.Cols.LEGEND_INFO_FIELD_SPECIALITY_ID, getLongSpecialityId());
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
        dest.writeLong(mLongSpecialityId);
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
