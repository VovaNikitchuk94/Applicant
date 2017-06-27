package com.example.vova.applicant.model;

import android.content.ContentValues;

public abstract class BaseEntity{

    private long mId = -1;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public abstract ContentValues getContentValues();

//    public abstract void setDataOnHolder(RecyclerView.ViewHolder)

//    public abstract int getGetItemResource()

}
