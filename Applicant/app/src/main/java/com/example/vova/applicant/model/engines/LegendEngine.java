package com.example.vova.applicant.model.engines;

import android.content.Context;
import android.util.Log;

import com.example.vova.applicant.model.LegendInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.LegendDBWrapper;

import java.util.ArrayList;

public class LegendEngine extends BaseEngine {

    public LegendEngine(Context context) {
        super(context);
    }

    public void updateLegend(LegendInfo legendInfo) {
        LegendDBWrapper legendDBWrapper = new LegendDBWrapper(getContext());
        legendDBWrapper.updateLegend(legendInfo);
    }

    public void addLegend(LegendInfo legendInfo) {
        LegendDBWrapper legendDBWrapper = new LegendDBWrapper(getContext());
        legendDBWrapper.addLegend(legendInfo);
    }

    public ArrayList<LegendInfo> getLegendsById(long nId) {
        Log.d("My", "LegendEngine ----------> getLegendsById");
        LegendDBWrapper legendDBWrapper = new LegendDBWrapper(getContext());
        return legendDBWrapper.getLegendsById(nId);
    }
}
