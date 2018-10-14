package pl.yahoo.pawelpiedel.data.place.remote;


import io.reactivex.Observable;
import pl.yahoo.pawelpiedel.data.place.Place;

public class PlaceRemoteDataSource {

    private final PlaceApi placeApi;

    public PlaceRemoteDataSource(PlaceApi placeApi) {
        this.placeApi = placeApi;
    }

    public Observable<Place> getPlace(String macAddress) {
        return placeApi.getPlace(macAddress);
    }
}
