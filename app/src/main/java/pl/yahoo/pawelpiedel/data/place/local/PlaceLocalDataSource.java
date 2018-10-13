package pl.yahoo.pawelpiedel.data.place.local;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import pl.yahoo.pawelpiedel.data.place.Place;
import timber.log.Timber;

public class PlaceLocalDataSource {

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public PlaceLocalDataSource(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    public void savePlace(Place place, String macAddress) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(place);
        prefsEditor.putString(macAddress, json);
        prefsEditor.apply();

        Timber.d("Place " + place + "saved with macAddress " + macAddress);
    }

    public Place getPlaceByMacAddress(String macAddress) {
        Timber.d("Searching for cached place with mac address " + macAddress);
        String json = sharedPreferences.getString(macAddress, "");

        Place place = gson.fromJson(json, Place.class);
        Timber.d("Cached place found " + place);

        return place;
    }
}
