package com.example.vova.applicant.model.engines;

import android.content.Context;

public abstract class BaseEngine {

    private Context mContext;

    public BaseEngine(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }
}
