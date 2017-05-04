package com.example.vova.applicant.model.engines;

import android.content.Context;

import com.example.vova.applicant.model.CitiesInfo;
import com.example.vova.applicant.model.wrappers.dbWrappers.CitiesInfoDBWrapper;

import java.util.ArrayList;

public class CitiesInfoEngine extends BaseEngine {

    public CitiesInfoEngine(Context context) {
        super(context);
    }

    public ArrayList<CitiesInfo> getAllCities() {
        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
        return citiesInfoDBWrapper.getAllCities();
    }

    public void addCity(CitiesInfo citiesInfo) {
        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
        citiesInfoDBWrapper.addCity(citiesInfo);
    }

    public CitiesInfo getCityById(long nId){
        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
        CitiesInfo citiesInfo = citiesInfoDBWrapper.getCityById(nId);
        return citiesInfo;
    }

}
