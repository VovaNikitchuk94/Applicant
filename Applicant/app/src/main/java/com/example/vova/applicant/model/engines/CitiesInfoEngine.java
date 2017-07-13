package com.example.vova.applicant.model.engines;

import android.content.Context;
import android.util.Log;

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

    public ArrayList<CitiesInfo> getAllCitiesById(long nId) {
        Log.d("My", "CitiesInfoEngine ----------> getAllCitiesById");
        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
        return citiesInfoDBWrapper.getAllCitiesById(nId);
    }

    public void updateCity(CitiesInfo citiesInfo) {
        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
        citiesInfoDBWrapper.updateCity(citiesInfo);
    }

//    public void addItem(CitiesInfo ItemInfo) {
//        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
//        citiesInfoDBWrapper.addItem(ItemInfo);
//    }

    public CitiesInfo getCityById(long nId) {
        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
        return citiesInfoDBWrapper.getCityById(nId);
    }

    public ArrayList<CitiesInfo> getAllCitiesBySearchString(long nId, String strSearch) {
        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
        return citiesInfoDBWrapper.getAllCitiesBySearchString(nId, strSearch);
    }

    public ArrayList<CitiesInfo> getAllFavoriteCities() {
        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
        return citiesInfoDBWrapper.getAllFavoriteCities();
    }

    public void addAllCities(ArrayList<CitiesInfo> citiesInfos) {
        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
        citiesInfoDBWrapper.addAllItems(citiesInfos);
    }

    public void updateAllCitiies(ArrayList<CitiesInfo> citiesInfos) {
        CitiesInfoDBWrapper citiesInfoDBWrapper = new CitiesInfoDBWrapper(getContext());
        citiesInfoDBWrapper.updateAllItems(citiesInfos);
    }

}
